package org.sundo.list.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;

@Data @Builder
@AllArgsConstructor
public class RequestObservatory {

    private Long seq; // 연번

    private String obsnm; // 관측소명

    private String obstype; // 관측유형

    private String obscd; // 관측소 코드

    private String obinfo; // 관측 정보

    private String sbsncd; // 표준 유역 코드

    private String add; // 위치(주소)

    private double latitude; // 위도

    private double longitude; // 경도

    private String obsknd; // 관측방식

    private String mngorg; // 운영기관
}
