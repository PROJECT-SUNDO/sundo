package org.sundo.map.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.sundo.commons.ListData;
import org.sundo.commons.rests.JSONData;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.services.ObservatoryInfoService;

import java.util.List;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class ApiMapController {

    private final ObservatoryInfoService observatoryInfoService;

    @GetMapping("/observatory")
    public JSONData<List<Observatory>> getObservatory() {

        ObservatorySearch search = new ObservatorySearch();
        ListData<Observatory> data = observatoryInfoService.getList(search);

        return new JSONData<>(data.getItems());
    }
}
