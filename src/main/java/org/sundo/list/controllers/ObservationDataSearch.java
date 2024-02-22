package org.sundo.list.controllers;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * 관측 정보
 */
@Data
public class ObservationDataSearch {
    private int page = 1; // 기본값 1page
    private int limit = 10; // 0 : 설정에 있는 1페이지 게시글 갯수, 1이상 : 지정한 갯수
    private String obscd; // 관측소 코드
    private String type; // 관측 타입

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate sdate; // 시작날짜

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate edate; // 끝 날짜

    private String timeUnit; // 시간 단위



}
