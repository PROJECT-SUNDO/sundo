package org.sundo.wamis.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.sundo.wamis.constants.ApiURL;

import org.sundo.wamis.entities.*;
import org.sundo.wamis.repositories.FlwObservatoryRepository;
import org.sundo.wamis.repositories.RfObservatoryRepository;
import org.sundo.wamis.repositories.WlObservatoryRepository;


import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WamisApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final RfObservatoryRepository rfObservatoryRepository; // 강수량 관측소
    private final WlObservatoryRepository wlObservatoryRepository; // 수위 관측소
    private final FlwObservatoryRepository flwObservatoryRepository; // 유량 관측소

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
     * 수위 + 유량 관측소 목록 조회
     *
     */
    public List<WlfObservatory> getWlfObservatories() {
        String url = ApiURL.WLF_OBSERVATORY_LIST;


        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<WlfObservatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<WlfObservatory>>() {
            });
            List<WlfObservatory> items = result.getContent();

            wlfObservatoryRepository.saveAllAndFlush(items);

            return items;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 강수량 관측소 목록 조회
     *
     */
    public List<RfObservatory> getRfObservatories() {
        String url = ApiURL.RF_OBSERVATORY_LIST;

        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<RfObservatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<RfObservatory>>() {
            });
            List<RfObservatory> items = result.getContent();
            items.forEach(System.out::println);

            rfObservatoryRepository.saveAllAndFlush(items);

            return items;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 수위 + 유량 최근 1건 출력
     * @param obscd : 관측소 코드
     */
    public void updateWaterLevelFlow(String obscd) {
        String url = ApiURL.WATER_LEVEL_FLOW + "/" + obscd +".json";
        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<WaterLevelFlow> result = objectMapper.readValue(data,
                    new TypeReference<ApiResultList<WaterLevelFlow>>() {
                    });

            List<WaterLevelFlow> items = result.getContent();
            items.forEach(item -> item.setObscd(obscd));
            items.forEach(System.out::println);
            waterLevelFlowRepository.saveAllAndFlush(items);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }



}
