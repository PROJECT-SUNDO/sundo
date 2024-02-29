package org.sundo.main.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/")
    public String index() {
        return "redirect:/map";
    }
}
