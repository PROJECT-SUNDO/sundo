package org.sundo.dashboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.sundo.commons.ListData;
import org.sundo.commons.exceptions.ExceptionProcessor;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.services.ObservatoryInfoService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController implements ExceptionProcessor {

    private final ObservatoryInfoService observatoryInfoService;

    @GetMapping("/{type}")
    public String dashboard(@PathVariable("type") String type,
                            @ModelAttribute ObservatorySearch search,
                            Model model) {
        commonProcess(type, model);
        search.setType(type);
        if(!StringUtils.hasText(search.getOrder())){
            search.setOrder(type);
        }

        ListData<Observatory> data = observatoryInfoService.getList(search);
        List<Observatory> items = data.getItems();

        model.addAttribute("items", items);
        model.addAttribute("pagination", data.getPagination());

        return "front/dashboard/dashboard";
    }


    private void commonProcess(String mode, Model model){
        mode = StringUtils.hasText(mode) ? mode : "rf";
        String pageTitle = "강수량 대시보드";

        List<String> addScript = new ArrayList<>();
        List<String> addCommonScript = new ArrayList<>();
        List<String> addCommonCss = new ArrayList<>();
        List<String> addCss = new ArrayList<>();

        if(mode.equals("rf")){
            pageTitle = "강수량 대시보드";
        }else if(mode.equals("wl")){
            pageTitle = "수위 대시보드";
        }else if(mode.equals("flw")){
            pageTitle = "유량 대시보드";
        }

        addScript.add("dashboard/dashboard");
        addCss.add("dashboard/style");
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("addScript", addScript);
        model.addAttribute("addCommonScript", addCommonScript);
        model.addAttribute("addCss", addCss);
        model.addAttribute("addCommonCss", addCommonCss);
    }
}