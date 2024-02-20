package org.sundo.wamis.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.sundo.wamis.constants.ApiURL;

import org.sundo.wamis.entities.*;
import org.sundo.wamis.repositories.*;


import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
        String url = ApiURL.RF_OBSERVATORY_LIST;
        if (type.equals("wl")) {
            url = ApiURL.WL_OBSERVATORY_LIST;
        } else if (type.equals("flw")) {
            url = ApiURL.FLW_OBSERVATORY_LIST;
        }

        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<Observatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<Observatory>>() {});

            List<Observatory> items = result.getList();
            items.forEach(item -> item.setType(type));
            items.forEach(System.out::println);

            observatoryRepository.saveAllAndFlush(items);

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

            items.forEach(System.out::println);
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

        String url = ApiURL.PrecipitationFlow + timeUnit + "/" + obscd + "/" + getPeriod()+ ".json";

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



}
