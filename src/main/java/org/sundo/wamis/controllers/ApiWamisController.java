package org.sundo.wamis.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sundo.wamis.services.StatisticService;
import org.sundo.wamis.services.WamisApiService;

@RestController
@RequestMapping("/api/wamis")
@RequiredArgsConstructor
public class ApiWamisController {
    private final WamisApiService apiService;
    private final StatisticService statisticService;
    @GetMapping
    public void test() {
        apiService.getObservatories("rf");
        apiService.getObservatories("wl");
        apiService.getObservatories("flw");
        apiService.updateWaterLevelFlow( "10M", "1018683");
        apiService.updatePrecipitation( "10M", "10184100");

    }

    @GetMapping("/stat")
    public void test2() {
        statisticService.update();
    }

    @GetMapping("/test2")
    public void test2() {
        apiService.updateRf10M();
    }

    @GetMapping("/test3")
    public void test3() {
        apiService.updateWlFw10M();
    }
}
