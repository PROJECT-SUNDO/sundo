package org.sundo.list.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController {
	
	@GetMapping
	public String list(Model model) {
		return "front/list/list";  
	}
	

}
