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

    @NotBlank
    private String type; // 관측소 타입 - rf(강수량 관측소), wl(수위 관측소), flw(유량 관측소)


    private String bbsnnm; // 대권역명

    @NotBlank
    private String obsnm; // 관측소명

    private String clsyn; // 운영여부

    private String obsknd; // 관측방법

    private String sbsncd; // 표준유역코드

    private String mngorg; // 관할기관

    private double latitude; // 위도
    private double longitude; // 경도
}
