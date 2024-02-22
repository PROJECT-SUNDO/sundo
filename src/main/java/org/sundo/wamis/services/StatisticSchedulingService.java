package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class StatisticSchedulingService {
    private static Logger logger = Logger.getLogger(StatisticSchedulingService.class);
    private final StatisticService service;
    @Scheduled(fixedRate = 1L, timeUnit = TimeUnit.HOURS)
    public void process() {
        logger.info("통계 업데이트 시작");
        service.update();
        logger.info("통계 업데이트 종료");
    }
}
