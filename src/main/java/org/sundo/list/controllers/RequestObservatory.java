package org.sundo.list.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestObservatory {

    private String mode = "write";

    @NotBlank
    private String obscd; // 관측소 코드

    private String obsnm; // 관측소명

    @NotBlank
    private String obstype; // 관측유형

    private String sbsncd; // 표준 유역 코드

    private double latitude; // 위도

    private double longitude; // 경도

    private String obsknd; // 관측방식

    private String mngorg; // 운영기관
}
