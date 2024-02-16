/*
package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.sundo.wamis.entities.Observatory;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class WamisSchedulingService {
    private static Logger logger = Logger.getLogger(WamisSchedulingService.class);

    private final WamisApiService apiService;

    */
/**
     * 강수량, 수위 시간별 데이터 업데이트
     *
     * 시간별 데이터이므로 1시간 마다 자동 업데이트
     *//*


    @Scheduled(timeUnit = TimeUnit.HOURS, fixedRate = 1L)
    public void rfWlTask() {
        */
/* 강수량 업데이트 S*//*

        logger.info("강수량 업데이트 시작");
        List<Observatory> items = apiService.getObservatories("rf");

        if (items != null && !items.isEmpty()) {
            items.forEach(item -> apiService.updatePrecipitation(item.getObscd()));
        }//비어 있는 경우에 가져온다??? 비어있지 않은 경우에는??
        logger.info("강수량 데이터 업데이트 종료");
        */
/* 강수량 업데이트 E*//*

    }

}
*/
