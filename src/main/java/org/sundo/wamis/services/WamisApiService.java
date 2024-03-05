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
import org.sundo.wamis.entities.*;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WamisApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final ObservatoryRepository observatoryRepository; // 모든 관측소

    private final WaterLevelFlowRepository waterLevelFlowRepository; // 수위 + 유량 데이터
    private final PrecipitationRepository precipitationRepository; // 강수량 데이터


    /**
     * 모든 관측소
     * @param type
     * @return
     */
    public List<Observatory> getObservatories(String type) {
        QObservatory observatory = QObservatory.observatory;

        List<Observatory> observatories = (List<Observatory>)observatoryRepository.findAll(observatory.type.eq(type));
        if(observatories != null && !observatories.isEmpty()){
            return observatories;
        }

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
            items.forEach(item -> {
                item.setType(type);
                changeBbsnnm(item);
            });

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
            List<Observatory> items2 = items.stream().filter(s -> StringUtils.hasText(s.getAddr()) && (s.getAddr().contains("서울") || s.getAddr().contains("경기도"))).collect(Collectors.toList());

            items2.forEach(s -> {
                if(s.getType().equals("rf")){
                    updatePrecipitation("10M", s.getObscd());
                }else{
                    updateWaterLevelFlow("10M", s.getObscd());
                }

            });
            observatoryRepository.saveAllAndFlush(items2);

            return items2;
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

            QWaterLevelFlow waterLevelFlow = QWaterLevelFlow.waterLevelFlow;

            items.forEach(item -> {
                LocalDate date = LocalDate.parse(item.getYmdhm(), formatter);
                item.setYmd(date);
                int uid = Objects.hash(item.getWlobscd(), item.getYmdhm());
                item.setUid(uid);

                try{

                    waterLevelFlowRepository.saveAndFlush(item);
                }catch (Exception e){
                    // 이미 등록된 데이터의 경우
                    WaterLevelFlow _item =  waterLevelFlowRepository.findOne(waterLevelFlow.uid.eq(uid)).orElse(null);
                    if(_item != null){
                        double wl = item.getWl();
                        double fw = item.getFw();
                        if( wl != 0.0 && wl != _item.getWl()){
                            _item.setWl(wl);
                        }
                        if(fw != 0.0 && fw != _item.getFw()){
                            _item.setFw(fw);
                        }

                    }
                    waterLevelFlowRepository.flush();
                }

            });

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
                item.setUid(Objects.hash(item.getRfobscd(), item.getYmdhm(), "rf"));
                try{
                    precipitationRepository.saveAndFlush(item);
                }catch (Exception e){}

            });



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

        return SdateTime + "/" + EdateTime;
    }


    /**
     * 강수량 10분 실시간 데이터 업데이트
     *
     */
    public void updateRf10M() {
        List<Observatory> oItems = getObservatories("rf");

        for (Observatory oitem : oItems) {
            String url = String.format(ApiURL.RCT_PRECIPITATION, oitem.getObscd());
            String data = restTemplate.getForObject(url, String.class);
            Pattern pattern = Pattern.compile("\"rf\"\\s*:\\s*([^,]+),");
            Matcher matcher = pattern.matcher(data);
            if (matcher.find()) {
                String rf = matcher.group(1);
                if (StringUtils.hasText(rf)) {
                    oitem.setRf(Double.parseDouble(rf));
                }
            }



         }

        observatoryRepository.saveAllAndFlush(oItems);
    }

    /**
     * 수위 10분 실시간 데이터 업데이트
     *
     */
    public void updateWlFw10M() {
        List<Observatory> oItems = getObservatories("wl");
        List<Observatory> oItems2 = getObservatories("flw");

        oItems.addAll(oItems2);

        for (Observatory oitem : oItems) {
            String url = String.format(ApiURL.RCT_WATER_LEVEL_FLOW, oitem.getObscd());
            String data = restTemplate.getForObject(url, String.class);
            Pattern pattern1 = Pattern.compile("\"wl\"\\s*:\\s*\"?([^,\"]+)\"?,");
            Matcher matcher1 = pattern1.matcher(data);
            if (matcher1.find()) {
                String wl = matcher1.group(1);
                if (StringUtils.hasText(wl)) {
                    oitem.setWl(Double.parseDouble(wl));
                }
            }

            Pattern pattern2 = Pattern.compile("\"fw\"\\s*:\\s*\"?([^,\"]+)\"?,");
            Matcher matcher2 = pattern2.matcher(data);
            if (matcher2.find()) {
                String fw = matcher2.group(1);
                if (StringUtils.hasText(fw)) {
                    oitem.setFw(Double.parseDouble(fw));
                }
            }
        }

        observatoryRepository.saveAllAndFlush(oItems);
    }

    /**
     * 권역코드별 상류/하류 구분
     */
    private void changeBbsnnm(Observatory observatory){
        List<String> upstream = Arrays.asList("1001", "1002", "1003", "1004", "1005", "1006", "1007", "1016");
        List<String> downstream = Arrays.asList("1017", "1018", "1019");

        String middleCode = observatory.getSbsncd().substring(0, 4);
        if(upstream.contains(middleCode)){
            observatory.setBbsnnm("상류");
        }
        if(downstream.contains(middleCode)){
            observatory.setBbsnnm("하류");
        }
    }
    public void update() {
        List<Observatory> items = getObservatories("rf");
        List<Observatory> items2 = getObservatories("wl");
        List<Observatory> items3 = getObservatories("flw");
        items.addAll(items2);
        items.addAll(items3);

        items.forEach(s -> {
            if(s.getType().equals("rf")){
                updatePrecipitation("10M", s.getObscd());
            }else{
                updateWaterLevelFlow("10M", s.getObscd());
            }
        });
    }



}
