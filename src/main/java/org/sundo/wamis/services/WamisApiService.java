package org.sundo.wamis.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.sundo.configs.DbConfig;
import org.sundo.wamis.constants.ApiURL;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.WaterFlowLevel;
import org.sundo.wamis.entities.WaterLevel;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterFlowLevelRepository;
import org.sundo.wamis.repositories.WaterLevelRepository;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class WamisApiService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ObservatoryRepository observatoryRepository;
    private final WaterLevelRepository waterLevelRepository;
    private final WaterFlowLevelRepository waterFlowLevelRepository;

    /**
     * 관측소 목록 조회
     *
     */
    public List<Observatory> getObservatories(String type) {
        String url = ApiURL.RF_OBSERVATORY_LIST;
        if(type.equals("wl")) {
            url = ApiURL.WL_OBSERVATORY_LIST;
        } else if(type.equals("flw")) {
            url = ApiURL.FLW_OBSERVATORY_LIST;
        }
        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<Observatory> result = objectMapper.readValue(data, new TypeReference<ApiResultList<Observatory>>() {
            });
            List<Observatory> items = result.getList();

            // type 넣기
            items.forEach(item -> item.setType(type));

            observatoryRepository.saveAllAndFlush(items);

            return items;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 수위 시자료
     * @param obscd : 관측소 코드
     */
    public void updateWaterLevel(String obscd) {
        String url = ApiURL.WATER_LEVEL + "?obscd=" + obscd;
        String data = restTemplate.getForObject(URI.create(url), String.class);
        try {
            ApiResultList<WaterLevel> result = objectMapper.readValue(data,
                    new TypeReference<ApiResultList<WaterLevel>>() {
                    });

            List<WaterLevel> items = result.getList();
            items.forEach(item -> item.setObscd(obscd));
            items.forEach(System.out::println);
            waterLevelRepository.saveAllAndFlush(items);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 유량 실시간
     *
     * @param obscd : 관측소 코드
     */
    public void updateWaterFlowLevel(String obscd) {
        String url = ApiURL.WATER_FLOW_LEVEL + "?obscd=" + obscd;
        String data = restTemplate.getForObject(url, String.class);
        try {
            ApiResultList<WaterFlowLevel> result = objectMapper.readValue(data,
                    new TypeReference<ApiResultList<WaterFlowLevel>>() {
                    });

            List<WaterFlowLevel> items = result.getList();
            if (items != null && !items.isEmpty()) {
                WaterFlowLevel item = items.get(items.size() -1);
                item.setObscd(obscd);
                LocalTime time = LocalTime.now();
                item.setYmdhi(LocalDateTime.of(item.getYmd(), LocalTime.of(time.getHour(),
                        time.getMinute(), 0)));
                waterFlowLevelRepository.saveAndFlush(item);
            }
           /* items.forEach(item -> {
                item.setObscd(obscd);
                LocalTime time = LocalTime.now();
                item.setYmdhi(LocalDateTime.of(item.getYmd(), LocalTime.of(time.getHour(),
                        time.getMinute(), 0)));
                    });
            waterFlowLevelRepository.saveAllAndFlush(items);

            */

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }


}
