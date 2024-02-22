package org.sundo.dashboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sundo.commons.ListData;
import org.sundo.commons.Utils;
import org.sundo.commons.exceptions.ExceptionProcessor;
import org.sundo.dashboard.service.DashboardInfoService;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.services.ObservationInfoService;
import org.sundo.wamis.services.ObservatoryNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController implements ExceptionProcessor {

    private final DashboardInfoService dashboardInfoService;
    private final ObservationInfoService observationInfoService;

    @GetMapping
    public String dashboard(@ModelAttribute ObservatorySearch search, Model model){


        ListData<Observatory> data = dashboardInfoService.getRFList(search);


        List<String> addScript = new ArrayList<>();
        List<String> addCommonCss = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        addScript.add("dashboard/dashboard");
        addCss.add("dashboard/style");
        addCommonCss.add("common/style");

        model.addAttribute("addScript", addScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonCss", addCommonCss);

        model.addAttribute("items", data.getItems());
        model.addAttribute("pagination", data.getPagination());

        return "front/dashboard/dashboard";
    }
}
