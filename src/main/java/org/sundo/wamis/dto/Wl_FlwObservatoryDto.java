package org.sundo.wamis.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


/**
 *  수위 + 유량 관측소 (주소, 경도, 위도)
 */



@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wl_FlwObservatoryDto {

    private String wlobscd; // 관측소 코드

    private String type; // 관측소 타입 - rf(강수량 관측소), wl(수위 관측소), flw(유량 관측소)

    private String addr; // 주소

    private String etcaddr; // 나머지 주소

    private String lon; // 경도

    private String lat; // 위도

    private String attwl; // 관심 수위

    private String wrnwl; // 주의보 수위

    private String almwl; // 경보 수위

    private String srswl; // 심각 수위

    private String pfh; // 계획홍수위

    private String fstnyn; // 특보지점여부

}
