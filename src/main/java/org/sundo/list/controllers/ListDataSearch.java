package org.sundo.list.controllers;

import lombok.Data;

import java.util.List;

@Data
public class ListDataSearch {
    private int page = 1; // 기본값 1page
    private int limit = 10; // 0 : 설정에 있는 1페이지 게시글 갯수, 1이상 : 지정한 갯수

    private int obscd; // 관측소 코드
    private String obsnm; // 관측소 명
    private String type; // 관측 변수

    private List<Long> seq; // 게시글 번호

}