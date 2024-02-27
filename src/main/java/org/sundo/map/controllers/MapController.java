package org.sundo.map.controllers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.sundo.commons.ListData;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.list.controllers.RequestObservatory;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.services.ObservatoryInfoService;
import org.sundo.wamis.services.ObservatoryNotFoundException;


@Controller
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapController {

	private final ObservatoryInfoService observatoryInfoService;
	private final ObjectMapper om;
	private final ObservatoryRepository observatoryRepository;
	
	@GetMapping
	public String map(@ModelAttribute RequestObservatory form,
					  Model model) {
		commonProcess("map", model);

		return "front/map/map";
	}

	@GetMapping("/search/{type}")
	public String search(@PathVariable("type") String type,
						 @ModelAttribute ObservatorySearch search,
						 @ModelAttribute RequestObservatory form,
						 Model model){
		commonProcess("aside", model);
		search.setType(type);
		if(!StringUtils.hasText(search.getOrder())){
			search.setOrder(type);
		}
		ListData<Observatory> data = observatoryInfoService.getList(search);
		List<Observatory> items = data.getItems();
		System.out.println(items);
		model.addAttribute("items", items);
		model.addAttribute("pagination", data.getPagination());
		try {
			String json = om.writeValueAsString(items);
			model.addAttribute("json", json);
		} catch (JsonProcessingException e) {}

		return "front/map/aside";
	}

	@GetMapping("/popup/{type}")
	public String popup(@PathVariable("type") String type,
						@RequestParam("obscd") String obscd,
						Model model) {

		Observatory observatory = observatoryRepository.getOne(obscd, type).orElseThrow(ObservatoryNotFoundException::new);

		model.addAttribute("observatory", observatory);

		commonProcess("info", model);
		return "front/map/popup/" + type;
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
			addScript.add("map/map");
			addScript.add("map/aside");
			addScript.add("map/draw");
			addScript.add("map/markers");
			addCss.add("map/map");
		} else if (mode.equals("info")) {
			pageTitle = null;
			addScript.add("map/cctv");
			addCss.add("map/cctv");
		}else if(mode.equals("aside")){
			pageTitle = null;
			addCss.add("map/aside");
			addScript.add("map/ifm");
		}
		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("addScript", addScript);
		model.addAttribute("addCss", addCss);
	}
}
