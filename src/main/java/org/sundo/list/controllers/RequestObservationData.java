package org.sundo.list.controllers;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

/**
 * 관측 정보
 */
public class RequestObservationData {
    @NotBlank
    private String obscd; //관측소 코드
    @NotBlank
    private String ymdhm; ////년월일시분

    /**
     * 시간 단위
     * 10M
     * 1H
     * 1D
     * 1M
     * 1Y
     */
    private String timeUnit; // 시간 단위

    private double rf; // 강수량

    private double wl; // 수위

    private double fw; // 유량
}
