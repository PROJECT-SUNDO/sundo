package org.sundo.list.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.sundo.commons.ListData;
import org.sundo.commons.Utils;
import org.sundo.commons.exceptions.AlertBackException;
import org.sundo.commons.exceptions.ExceptionProcessor;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.services.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController implements ExceptionProcessor {

	private final Utils utils;
	private final ObservatorySaveService observatorySaveService;
	private final ObservatoryValidator observatoryValidator;
	private final ObservationInfoService observationInfoService;
	private final ObservatoryInfoService observatoryInfoService;
	private final ObservatorySettingValidator observatorySettingValidator;
	private final ObservatoryRepository observatoryRepository;
	private final ObservationSaveService observationSaveService;
	private final ObservatoryDeleteService observatoryDeleteService;
	private final ObservationDeleteService observationDeleteService;
	@GetMapping
	public String list (@ModelAttribute ObservatorySearch search, Model model){
		commonProcess("list", model);

		ListData<Observatory> data = observatoryInfoService.getList(search);

		model.addAttribute("items", data.getItems());
		model.addAttribute("pagination", data.getPagination());

		return "front/list/list";
	}

	/**
	 * 관측소명 클릭 시 상세페이지 팝업 페이지
	 */
	@GetMapping("/detail/{obscd}")
	public String detail (@PathVariable("obscd") String obscd, Model model){

		Observatory observatory = observatoryInfoService.get(obscd);
		model.addAttribute("observatory", observatory);

		return "front/list/detail";
	}

	/**
	 * 관측 정보
	 * - 선택 관측소 정보
	 * - 검색 영역
	 *   조회 기간, 단위(10분/1시간/일/월/년)
	 * - 목록 (검색 전 10분 단위 출력)
	 */
	@GetMapping("/info/{seq}")
	public String info (@PathVariable("seq") String seq, @ModelAttribute ObservationSearch search,
						Model model) {
		commonProcess("info", model);
		String obscd = utils.getParam("obscd");
		String type = utils.getParam("type");
		RequestObservatory form = observatoryInfoService.getRequest(obscd, type);
		search.setObscd(form.getObscd());
		search.setType(form.getType());

		model.addAttribute("requestObservatory", form);

		return "front/list/info";
	}

	/**
	 * 관측소 등록
	 */
	@GetMapping("/add")
	public String add (@ModelAttribute RequestObservatory form, Model model) {
		commonProcess("list", model);


		return "front/list/write";
	}


	/**
	 * 관측소 수정
	 */
	@GetMapping("/update/{obscd}/{type}")
	public String update (@PathVariable("obscd") String obscd, @PathVariable("type") String type, Model model){
		commonProcess("list", model);

		RequestObservatory form = observatoryInfoService.getRequest(obscd, type);
		model.addAttribute("requestObservatory", form);

		return "front/list/update";
	}


	/**
	 * 관측소 저장 및 수정 하기
	 */
	@PostMapping("/save")
	public String save (@Valid RequestObservatory form, Errors errors, Model model){
		commonProcess("list", model);
		//form에서 검증하고 실패하면 errors로 보냄

		observatoryValidator.validate(form, errors);

		if (errors.hasErrors()) {
			//실패할시 다시 양식데이터를 보여주고 필요한부분을 다시 쓰게 함
			return "front/list/" + form.getMode();
		}

		observatorySaveService.save(form);

		return "redirect:/list";
	}


	/**
	 * 관측소 삭제 -> 삭제 여부 팝업
	 */

	@GetMapping("/delete/{obscd}")
	public String delete(@PathVariable("obscd") String obscd,
							@ModelAttribute RequestObservation form,
							Model model){
		commonProcess("delObs", model);
		form.setObscd(obscd);

		return "front/list/popup/delete";
	}

	@PostMapping("/delete")
	public String delete(RequestObservation form, Model model){
		commonProcess("delObs", model);
		String obscd = form.getObscd();

		observatoryDeleteService.delete(obscd);

		String script = "alert('삭제되었습니다.');"+ "parent.location.reload();";

		model.addAttribute("script", script);
		return "common/_execute_script";
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
		if(type.equals("wl")){
			search.setOutlier(form.getWrnwl());
		}else{
			search.setOutlier(form.getOutlier());
		}
		search.setType(form.getType());
		search.setOut(true);

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
			throw new AlertBackException("올바르지 않은 요청입니다.", HttpStatus.BAD_REQUEST);
		}

		observatorySaveService.saveOutlier(form);
		String script = "alert('저장되었습니다.');"
				+"parent.location.reload();";
		model.addAttribute("script", script);

		return "common/_execute_script";

	}

	@GetMapping("/setting/edit/{seq}")
	public String editData(@PathVariable("seq")Long seq,
						   @ModelAttribute RequestObservation form,
						   Model model){
		commonProcess("setEdit", model);
		String type = utils.getParam("type");
		form.setSeq(seq);
		if("rf".equals(type)){

			Precipitation item = observationInfoService.getPre(seq);
			String obscd = item.getRfobscd();
			Observatory observatory = observatoryRepository.getOne(obscd, type).orElseThrow(ObservatoryNotFoundException::new);

			form.setRf(item.getRf());
			form.setObscd(item.getRfobscd());
			form.setType(type);
			form.setYmdhm(item.getYmdhm());
			form.setObsnm(observatory.getObsnm());
		}else{
			WaterLevelFlow item = observationInfoService.getWLF(seq);
			String obscd = item.getWlobscd();
			Observatory observatory = observatoryRepository.getOne(obscd, type).orElseThrow(ObservatoryNotFoundException::new);

			form.setYmdhm(item.getYmdhm());
			form.setType(type);
			form.setObscd(item.getWlobscd());
			form.setFw(item.getFw());
			form.setWl(item.getWl());
			form.setObsnm(observatory.getObsnm());
		}

		return "front/list/observation_edit";
	}

	@PostMapping("/setting/edit")
	public String editDataPs(@Valid RequestObservation form,
							 Errors errors,
							 Model model){

		commonProcess("setEdit", model);

		if(errors.hasErrors()){
			throw new AlertBackException("올바르지 않은 요청입니다", HttpStatus.BAD_REQUEST);
		}

		observationSaveService.edit(form);

		String script = "alert('저장되었습니다.');"
				+"parent.location.reload();";
		model.addAttribute("script", script);

		return "common/_execute_script";
	}

	@GetMapping("/setting/delete/{seq}")
	public String deleteObs(@PathVariable("seq") Long seq,
							@ModelAttribute RequestObservation form,
							Model model){
		commonProcess("delObs", model);
		String type = utils.getParam("type");
		form.setSeq(seq);
		form.setType(type);

		return "front/list/popup/delete_obs";
	}

	@PostMapping("/setting/delete")
	public String deleteObsPs(RequestObservation form, Model model){
		commonProcess("delObs", model);
		String type = form.getType();
		Long seq = form.getSeq();

		observationDeleteService.delete(seq, type);

		String script = "alert('삭제되었습니다.');"+ "parent.location.reload();";

		model.addAttribute("script", script);
		return "common/_execute_script";
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
			addCommonCss.add("common/style");
		}else if (mode.equals("setting")){
			pageTitle = "환경설정";
			addCss.add("list/setting");
			addScript.add("list/setting");
		}else if (mode.equals("setEdit")){
			pageTitle = "관측값 수정";
			addCss.add("list/setting");
			addCss.add("list/edit_obs");
			addScript.add("list/edit_obs");
		}else if (mode.equals("delObs")){
			pageTitle = null;
			addCss.add("list/delete_obs");
			addScript.add("list/delete_obs");
		}else if(mode.equals("info")) {
			addCss.add("list/info");
			addCss.add("list/style");
			addScript.add("list/info");
			addCommonScript.add("api");
		}

		model.addAttribute("addCss", addCss);
		model.addAttribute("addScript", addScript);
		model.addAttribute("addCommonCss", addCommonCss);
		model.addAttribute("addCommonScript", addCommonScript);
		model.addAttribute("pageTitle", pageTitle);
	}

}