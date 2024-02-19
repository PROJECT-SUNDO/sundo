package org.sundo.list.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@AllArgsConstructor
public class RequestObservatory {

    private Long seq;

    private String obsnm; // 관측소명
    private String obscd; // 관측소코드
    private String type; // 관측유형

    private String sbsncd; // 표준 유역 코드

    private double latitude; // 위도

    private double longitude; // 경도

    private String obsknd; // 관측방법

    private String mngorg; // 운영기관

    private double outlier; // 이상치
    
    private boolean clsyn; // 사용여부
}
