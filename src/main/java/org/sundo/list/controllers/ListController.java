package org.sundo.list.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.sundo.commons.ListData;
import org.sundo.commons.Utils;
import org.sundo.list.service.ListInfoService;
import org.sundo.list.services.ListSaveService;
import org.sundo.wamis.entities.Observation;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.services.ObservationInfoService;
import org.sundo.wamis.services.ObservatoryInfoService;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController {

	private final Utils utils;
	private final ListInfoService listInfoService;
	private final ListSaveService listSaveService;
	private final ObservatoryValidator observatoryValidator;
	private final ObservationInfoService observationInfoService;
	private final ObservatoryInfoService observatoryInfoService;
	private final ObservatorySettingValidator observatorySettingValidator;

	@GetMapping
	public String list (@ModelAttribute ListDataSearch search, Model model){
		commonProcess("list", model);

		ListData<Observatory> data = listInfoService.getList(search);

		model.addAttribute("items", data.getItems());
		model.addAttribute("pagination", data.getPagination());

		return "front/list/list";
	}

	/**
	 * 관측소명 클릭 시 상세페이지 팝업 페이지
	 */
	@GetMapping("/detail/{seq}")
	public String detail (@PathVariable("seq") Long seq, Model model){
		return "front/list/detail";
	}

	/**
	 * 관측 정보 클릭 시 정보 확인 페이지
	 * - 선택 관측소 정보
	 * - 검색 영역
	 *   조회 기간, 단위(10분/1시간/일/월/년)
	 * - 목록 (검색 전 10분 단위 출력)
	 */
	@GetMapping("/info/{seq}")
	public String info (@PathVariable("seq") Long seq, Model model){
		return "front/list/info";
	}

	/**
	 * 관측소 등록
	 */
	@GetMapping("/add")
	public String add (@ModelAttribute RequestObservatory form, Model model) {
		commonProcess("write", model);


		return "front/list/write";
	}


	/**
	 * 관측소 수정
	 */
	@GetMapping("/update/{seq}")
	public String update (@PathVariable("seq") Long seq, Model model){
		return "front/list/update";
	}


	/**
	 * 관측소 저장 및 수정 하기
	 */
	@PostMapping("/save")
	public String save (@Valid RequestObservatory form, Errors errors, Model model){
		commonProcess(form.getMode(), model);
		//form에서 검증하고 실패하면 errors로 보냄

		observatoryValidator.validate(form, errors);

		if (errors.hasErrors()) {
			//실패할시 다시 양식데이터를 보여주고 필요한부분을 다시 쓰게 함
			return "front/list/" + form.getMode();
		}

		listSaveService.save(form);

		return "redirect:/list";
	}


	/**
	 * 관측소 삭제 -> 삭제 여부 팝업
	 */
	@GetMapping("/delete/{seq}")
	public String delete (@PathVariable("seq") Long seq, Model model){
				return "front/list/delete";
			}

	/**
	 * 환경설정
	 * - 사용여부
	 * - 이상치 기준 설정
	 * - 조회 기간 검색
	 * - 이상 내역 목록
	 */
	@GetMapping("/setting/{seq}")
	public String setting(@PathVariable("seq") String seq,
						  @ModelAttribute ObservationSearch search,
						  Model model) {
		commonProcess("setting", model);

		String obscd = utils.getParam("obscd");
		String type = utils.getParam("type");
		RequestObservatory form = observatoryInfoService.getRequest(obscd, type);
		search.setObscd(form.getObscd());
		search.setOutlier(form.getOutlier());
		search.setType(form.getType());

		if(type.equals("rf")){
			ListData<Precipitation> data = observationInfoService.getRFList(search);
			model.addAttribute("items", data.getItems());
			model.addAttribute("pagination", data.getPagination());
		}else{
			ListData<WaterLevelFlow> data = observationInfoService.getWLFList(search);
			model.addAttribute("items", data.getItems());
			model.addAttribute("pagination", data.getPagination());
		}


		model.addAttribute("requestObservatory", form);
		return "front/list/setting";
	}

	@PostMapping("/setting/save")
	public String saveSetting(@Valid RequestObservatory form, Errors errors, Model model){
		commonProcess("setting", model);

		observatorySettingValidator.validate(form, errors);

		if(errors.hasErrors()){
			return "front/list/setting";
		}


		return "";
	}

	/**
	 * 공통 작업
	 */
	public void commonProcess (String mode, Model model){
		mode = StringUtils.hasText(mode) ? mode : "list";
		String pageTitle = "목록";

		List<String> addCommonScript = new ArrayList<>();
		List<String> addScript = new ArrayList<>();
		List<String> addCommonCss = new ArrayList<>();
		List<String> addCss = new ArrayList<>();

		if(mode.equals("list")) {
			pageTitle = "목록";
			addScript.add("list/list");
			addCss.add("list/style");
		}else if (mode.equals("setting")){
			pageTitle = "환경설정";
			addCss.add("list/setting");
		}

		model.addAttribute("addCss", addCss);
		model.addAttribute("addScript", addScript);
		model.addAttribute("addCommonCss", addCommonCss);
		model.addAttribute("addCommonScript", addCommonScript);
		model.addAttribute("pageTitle", pageTitle);
	}
}