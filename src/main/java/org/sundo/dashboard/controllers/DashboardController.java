package org.sundo.dashboard.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    @GetMapping
    public String dashboard(Model model){
        List<String> addScript = new ArrayList<>();
        addScript.add("dashboard/dashboard");
        System.out.println("addScript = " + addScript);
        model.addAttribute("addScript", addScript);

        return "front/dashboard/dashboard";
    }
}