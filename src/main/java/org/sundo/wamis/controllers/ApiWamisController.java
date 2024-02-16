package org.sundo.wamis.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sundo.wamis.services.WamisApiService;

@RestController
@RequestMapping("/api/wamis")
@RequiredArgsConstructor
public class ApiWamisController {
    private final WamisApiService apiService;
    @GetMapping("/test")
    public void test() {
//        apiService.getObservatories();
//        apiService.getObservatories("rf");
//        apiService.getObservatories("wl");
//        apiService.getObservatories("flw");
//        apiService.updateWaterLevel("1001602");
//        apiService.updatePrecipitation("10011100");
        apiService.updateWaterFlowLevel("1001670");

    }


}
