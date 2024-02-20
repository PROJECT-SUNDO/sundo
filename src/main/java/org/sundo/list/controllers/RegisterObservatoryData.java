package org.sundo.list.controllers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterObservatoryData {

    private String seq;

    @NotBlank
    private String obsnm;//관측소 이름

    @NotBlank
    private String obstype; // 관측유형

    @NotBlank
    private String sbsncd; // 표준유역코드

    @NotBlank
    private double latitude; // 위도

    @NotBlank
    private double longitude; // 경도

    @NotBlank
    private String obsknd; // 관측방식

    @NotBlank
    private String mngorg; // 운영기관
}
