package org.sundo.map.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/map")
public class MapController {
	
	@GetMapping()
	public String map(Model model) {
		commonProcess("map", model);
		
		return "front/map/map";
	}
	
	private void commonProcess(String mode, Model model) {
		mode = StringUtils.hasText(mode) ? mode : "map";
		String pageTitle = "지도";
		
		List<String> addCommonScript = new ArrayList<>();
		List<String> addScript = new ArrayList<>();
		List<String> addCommonCss = new ArrayList<>();
		List<String> addCss = new ArrayList<>();
		
		if(mode.equals("map")) {
			pageTitle = "지도";
			addCommonScript.add("map");
		}
		
		model.addAttribute("addCommonScript", addCommonScript);
	}
}
