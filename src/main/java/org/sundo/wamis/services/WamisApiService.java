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
import org.sundo.wamis.repositories.FlwObservatoryRepository;
import org.sundo.wamis.repositories.RfObservatoryRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;
import org.sundo.wamis.repositories.WlObservatoryRepository;


import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WamisApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final RfObservatoryRepository rfObservatoryRepository; // 강수량 관측소
    private final WlObservatoryRepository wlObservatoryRepository; // 수위 관측소
    private final FlwObservatoryRepository flwObservatoryRepository; // 유량 관측소

    private final WaterLevelFlowRepository waterLevelFlowRepository; // 수위 + 유량 데이터

    /**
     * 수위 관측소 목록
     *
     */
    public List<WlObservatory> getWlObservatories() {
        String url = ApiURL.WL_OBSERVATORY_LIST;

        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<WlObservatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<WlObservatory>>() {
            });
            List<WlObservatory> items = result.getList();

            wlObservatoryRepository.saveAllAndFlush(items);

            return items;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 유량 관측소 목록
     *
     */
    public List<FlwObservatory> getFlwObservatories() {
        String url = ApiURL.FLW_OBSERVATORY_LIST;


        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<FlwObservatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<FlwObservatory>>() {
            });
            List<FlwObservatory> items = result.getList();

            flwObservatoryRepository.saveAllAndFlush(items);

            return items;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 강수량 관측소 목록
     *
     */
    public List<RfObservatory> getRfObservatories() {
        String url = ApiURL.RF_OBSERVATORY_LIST;

        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<RfObservatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<RfObservatory>>() {
            });
            List<RfObservatory> items = result.getList();
            items.forEach(System.out::println);

            rfObservatoryRepository.saveAllAndFlush(items);

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
     * 날짜 조회 임시
     * 기본값 : 현재 기준 24시간 전까지
     */
    public String getPeriod() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmm");
        String EdateTime = SDF.format(calendar.getTime()); // 현재
        calendar.add(Calendar.DATE, -1);
        String SdateTime = SDF.format(calendar.getTime()); // 하루 전

        return SdateTime + "/" + EdateTime;

    }



}
