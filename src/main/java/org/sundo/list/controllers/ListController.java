package org.sundo.list.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/list")
@RequiredArgsConstructor
public class ListController {
	
	@GetMapping
	public String list(@ModelAttribute RequestObservatory form, Model model) {
		commonProcess("list", model);

		return "front/list/list";  
	}

	/**
	 * 관측소명 클릭 시 상세페이지 팝업 페이지
	 */
	@GetMapping("/detail/{seq}")
	public String detail(@PathVariable("seq") Long seq, Model model) {
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
	public String info(@PathVariable("seq") Long seq, Model model) {
		return "front/list/info";
	}

	/**
	 * 관측소 등록
	 */
	@GetMapping("/add")
	public String add(Model model) {
		return null;
	}

	/**
	 * 관측소 수정
	 */
	@GetMapping("/update/{seq}")
	public String update(@PathVariable("seq") Long seq ,Model model) {
		return "front/list/update";
	}
	/**
	 * 관측소 저장하기
	 */
	@PostMapping("save")
	public String save(Model model) {
		return "redirect:/list";
	}

	/**
	 * 관측소 삭제 -> 삭제 여부 팝업
	 */
	@GetMapping("/delete/{seq}")
	public String delete(@PathVariable("seq") Long seq, Model model) {
		return null;
	}

	/**
	 * 환경설정
	 * - 사용여부
	 * - 이상치 기준 설정
	 * - 조회 기간 검색
	 * - 이상 내역 목록
	 */
	@GetMapping("/setting/{seq}")
	public String setting(@PathVariable("seq") Long seq, Model model) {
		return "front/list/setting";
	}

	/**
	 * 공통 작업
	 */
	public void commonProcess(String mode, Model model) {
		mode = StringUtils.hasText(mode) ? mode :  "list";
		String pageTitle = "목록";

		List<String> addCommonScript = new ArrayList<>();
		List<String> addScript = new ArrayList<>();
		List<String> addCommonCss = new ArrayList<>();
		List<String> addCss = new ArrayList<>();

		if(mode.equals("list")) {
			pageTitle = "목록";
			addScript.add("list/style");
			addCss.add("list/style");
		}

		model.addAttribute("addCss", addCss);
		model.addAttribute("addScript", addScript);
		model.addAttribute("addCommonCss", addCommonCss);
		model.addAttribute("addCommonScript", addCommonScript);
		model.addAttribute("pageTitle", pageTitle);
	}


	

}
