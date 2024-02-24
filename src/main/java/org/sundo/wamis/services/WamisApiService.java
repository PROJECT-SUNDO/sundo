package org.sundo.wamis.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.sundo.wamis.constants.ApiURL;
import org.sundo.wamis.dto.RfObservatoryDto;
import org.sundo.wamis.dto.Wl_FlwObservatoryDto;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.entities.WaterLevelFlow10M;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterLevelFlow10MRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WamisApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final ObservatoryRepository observatoryRepository; // 모든 관측소

    private final WaterLevelFlowRepository waterLevelFlowRepository; // 수위 + 유량 데이터
    private final PrecipitationRepository precipitationRepository; // 강수량 데이터

    private final WaterLevelFlow10MRepository waterLevelFlow10MRepository; // 수위 + 유량 데이터


    /**
     * 모든 관측소
     * @param type
     * @return
     */
    public List<Observatory> getObservatories(String type) {
        String url = ApiURL.RF_OBSERVATORY_LIST;
        String detailUrl = ApiURL.RF_OBSERVATORY_DETAIL;
        if (type.equals("wl")) {
            url = ApiURL.WL_OBSERVATORY_LIST;
            detailUrl = ApiURL.WL_FLW_OBSERVATORY_DETAIL;
        } else if (type.equals("flw")) {
            url = ApiURL.FLW_OBSERVATORY_LIST;
            detailUrl = ApiURL.WL_FLW_OBSERVATORY_DETAIL;
        }

        String data = restTemplate.getForObject(URI.create(url), String.class);
        String detailData = restTemplate.getForObject(URI.create(detailUrl), String.class);
        try {
            ApiResultList<Observatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<Observatory>>() {});

            List<Observatory> items = result.getList();
            items.forEach(item -> item.setType(type));

            // 상세 데이터
            if(type.equals("rf")){
                ApiDataResultList<RfObservatoryDto> detailResult = objectMapper.readValue(detailData, new TypeReference<ApiDataResultList<RfObservatoryDto>>() {
                });
                List<RfObservatoryDto> details = detailResult.getContent();
                details.forEach(detail -> detail.setType(type));

                // obscd가 같을 때
                List<RfObservatoryDto> matchingDetails = details.stream()
                        .filter(detail -> {
                            List<Observatory> items2 = items;

                            return items2.stream()
                                    .anyMatch(item -> item.getObscd().equals(detail.getRfobscd()));
                        })
                        .collect(Collectors.toList());

                for (Observatory item : items) {
                    matchingDetails.stream()
                            .filter(detail -> item.getObscd().equals(detail.getRfobscd()))
                            .findFirst()
                            .ifPresent(detail -> {
                                item.setLon(detail.getLon());
                                item.setLat(detail.getLat());
                                item.setAddr(detail.getAddr());
                                item.setEtcaddr(detail.getEtcaddr());
                                item.setOutlier(3.5);

                            });
                }

                // 상세 데이터
            } else if(type.equals("wl") || type.equals("flw")){
                ApiDataResultList<Wl_FlwObservatoryDto> detailResult = objectMapper.readValue(detailData, new TypeReference<ApiDataResultList<Wl_FlwObservatoryDto>>() {
                });
                List<Wl_FlwObservatoryDto> details = detailResult.getContent();
                details.forEach(detail -> detail.setType(type));

                // obscd가 같을 때
                List<Wl_FlwObservatoryDto> matchingDetails = details.stream()
                        .filter(detail -> {
                            List<Observatory> items2 = items;

                            return items2.stream()
                                    .anyMatch(item -> item.getObscd().equals(detail.getWlobscd()));
                        })
                        .collect(Collectors.toList());

                for (Observatory item : items) {
                    matchingDetails.stream()
                            .filter(detail -> item.getObscd().equals(detail.getWlobscd()))
                            .findFirst()
                            .ifPresent(detail -> {
                                item.setLon(detail.getLon());
                                item.setLat(detail.getLat());
                                item.setAddr(detail.getAddr());
                                item.setEtcaddr(detail.getEtcaddr());

                                if(type.equals("wl")) {
                                    item.setAttwl(detail.getAttwl()); // 관심
                                    item.setWrnwl(detail.getWrnwl()); // 주의보
                                    item.setAlmwl(detail.getAlmwl()); // 경보
                                    item.setSrswl(detail.getSrswl()); // 심각
                                    item.setPfh(detail.getPfh()); // 계획홍수위
                                    item.setFstnyn(detail.getFstnyn()); // 특보지점여부
                                }else{
                                    item.setOutlier(648.31);
                                }
                            });
                }
            }
            List<Observatory> items2 = items.stream().filter(s -> StringUtils.hasText(s.getAddr()) && (s.getAddr().contains("서울") || s.getAddr().contains("경기도"))).toList();
            observatoryRepository.saveAllAndFlush(items2);

            return items;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }




    /**
     * 수위 + 유량 데이터
     * 기간별 출력
     *
     * @param timeUnit : 단위별 출력
     *  - 10M : 10분
     *  - 1H : 1시간
     *  - 1D : 1일
     * @param obscd : 관측소 코드
     */
    public void updateWaterLevelFlow(String timeUnit, String obscd) {
        timeUnit = StringUtils.hasText(timeUnit) ? timeUnit : "10M";
        if(timeUnit.equals("1H")) {
            timeUnit = "1H";
        } else if (timeUnit.equals("1D")) {
            timeUnit = "1D";
        }

        String url = ApiURL.WATER_LEVEL_FLOW + timeUnit + "/" + obscd + "/" + getPeriod()+ ".json";

        String data = restTemplate.getForObject(URI.create(url), String.class);


        try {
            ApiDataResultList<WaterLevelFlow> result = objectMapper.readValue(data,
                    new TypeReference<ApiDataResultList<WaterLevelFlow>>() {
                    });

            List<WaterLevelFlow> items = result.getContent();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            items.forEach(item -> {
                LocalDate date = LocalDate.parse(item.getYmdhm(), formatter);
                item.setYmd(date);
            });



            waterLevelFlowRepository.saveAllAndFlush(items);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 강수량 데이터
     * 기간별 출력
     *
     * @param timeUnit : 단위별 출력
     *  - 10M : 10분
     *  - 1H : 1시간
     *  - 1D : 1일
     * @param obscd : 관측소 코드
     */





    public void updatePrecipitation(String timeUnit, String obscd) {
        timeUnit = StringUtils.hasText(timeUnit) ? timeUnit : "10M";
        if(timeUnit.equals("1H")) {
            timeUnit = "1H";
        } else if (timeUnit.equals("1D")) {
            timeUnit = "1D";
        }

        String url = ApiURL.PRECIPITATION + timeUnit + "/" + obscd + "/" + getPeriod()+ ".json";

        String data = restTemplate.getForObject(URI.create(url), String.class);


        try {
            ApiDataResultList<Precipitation> result = objectMapper.readValue(data,
                    new TypeReference<ApiDataResultList<Precipitation>>() {
                    });

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            List<Precipitation> items = result.getContent();
            items.forEach(item -> {
                LocalDate date = LocalDate.parse(item.getYmdhm(), formatter);
                item.setYmd(date);
            });

            precipitationRepository.saveAllAndFlush(items);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void updateWaterLevelFlow(String mode, String timeUnit, String obscd) {
        timeUnit = StringUtils.hasText(timeUnit) ? timeUnit : "10M";
        if(timeUnit.equals("1H")) {
            timeUnit = "1H";
        } else if (timeUnit.equals("1D")) {
            timeUnit = "1D";
        }

        String url = ApiURL.WATER_LEVEL_FLOW + timeUnit + "/" + obscd + "/" + getPeriod(timeUnit)+ ".json";

        System.out.println("링 = " + url);
        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiDataResultList<WaterLevelFlow10M> result = objectMapper.readValue(data,
                    new TypeReference<ApiDataResultList<WaterLevelFlow10M>>() {
                    });

            List<WaterLevelFlow10M> items = result.getContent();
   /*         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
            items.forEach(item -> {
                LocalDate date = LocalDate.parse(item.getYmdhm(), formatter);
                item.setYmd(date);
            });*/
            items.forEach(System.out::println);

            waterLevelFlow10MRepository.saveAllAndFlush(items);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 날짜 조회 임시
     * 기본값 : 현재 기준 24시간 전까지
     */
    public String getPeriod() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmm");
        String EdateTime = SDF.format(calendar.getTime()); // 현재
        calendar.add(Calendar.MINUTE, -4); // 딜레이 4분으로 설정
        EdateTime = SDF.format(calendar.getTime());
        EdateTime = EdateTime.substring(0, 11) + "0";
        calendar.add(Calendar.DATE, -1);

        String SdateTime = SDF.format(calendar.getTime()); // 하루 전
        calendar.add(Calendar.MINUTE, -4);
        SdateTime = SDF.format(calendar.getTime());
        SdateTime = SdateTime.substring(0, 11) + "0";

        return SdateTime + "/" + EdateTime;
    }

    /**
     * 10M - 14일
     * 1H - 30일
     * 1D - 30일
     */
    public String getPeriod(String timeUnit) {
        timeUnit = StringUtils.hasText(timeUnit) ? timeUnit : "10M";
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmm");
        String EdateTime = SDF.format(calendar.getTime()); // 현재
        calendar.add(Calendar.MINUTE, -4); // 딜레이 4분으로 설정
        EdateTime = SDF.format(calendar.getTime());
        EdateTime = EdateTime.substring(0, 11) + "0";
        if(timeUnit.equals("10M")) {
            calendar.add(Calendar.DATE, -14);
        } else if(timeUnit.equals("1H") || timeUnit.equals("1D")) {
            calendar.add(Calendar.DATE, -30);
        }

        String SdateTime = SDF.format(calendar.getTime());
        calendar.add(Calendar.MINUTE, -4);
        SdateTime = SDF.format(calendar.getTime());
        SdateTime = SdateTime.substring(0, 11) + "0";

        System.out.println("날짜 = " + SdateTime + "/" + EdateTime);

        return SdateTime + "/" + EdateTime;
    }



}
