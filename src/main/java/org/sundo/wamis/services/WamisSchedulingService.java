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


    /**
     * 강수량, 수위 시간별 데이터 업데이트
     *
     * 10분마다 업데이트
     */
    @Scheduled(fixedRate = 10L, timeUnit = TimeUnit.MINUTES)
    public void updateRf() {
        apiService.updateRf10M();
    }

    @Scheduled(fixedRate = 10L, timeUnit = TimeUnit.MINUTES)
    public void updateWlFw() {
        apiService.updateWlFw10M();
    }
}

