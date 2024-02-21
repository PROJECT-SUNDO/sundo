package org.sundo.list.controllers;

import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

import javax.persistence.Column;

public class RequestObservation {
    @NotBlank
    private String obscd; //관측소 코드
    @NotBlank
    private String ymdhm; ////년월일시분

    private double rf; // 강수량

    private double wl; // 수위

    private double fw; // 유량

}
