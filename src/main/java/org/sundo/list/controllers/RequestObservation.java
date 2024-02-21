package org.sundo.list.controllers;

import lombok.Data;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

import javax.persistence.Column;

@Data
public class RequestObservation {

    private Long seq; // 관측값 시퀀스
    @NotBlank
    private String obscd; //관측소 코드

    private String obsnm; // 관측소 이름

    private String type; // 관측 타입

    @NotBlank
    private String ymdhm; ////년월일시분

    private Double rf; // 강수량

    private Double wl; // 수위

    private Double fw; // 유량

}
