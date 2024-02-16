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
//          apiService.getRfObservatories();
//        apiService.getWlfObservatories();
//        apiService.updatePrecipitation("10011100");
//        apiService.updateWaterLevelFlow("1018683");
        apiService.getRfObservatories("10011100");

    }


}
