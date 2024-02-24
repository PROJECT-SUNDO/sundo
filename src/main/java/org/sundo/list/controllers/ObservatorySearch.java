package org.sundo.list.controllers;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

@Data
public class ObservatorySearch {
    private int page = 1; // 기본값 1page
    private int limit = 10; // 0 : 설정에 있는 1페이지 게시글 갯수, 1이상 : 지정한 갯수

    private String obscd; // 관측소 코드
    private String obsnm; // 관측소 명
    /*
    * 검색 옵션
    *
    * ALL :
    * RF : 강수량
    * WL : 수위
    * WLF : 유량
    * */
    private String type; // 관측 변수

    private List<Long> seq; // 게시글 번호
    private String order; // 나열 기준

    private String lon; // 경도

    private String lat; // 위도

}
