package org.sundo.wamis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 강수량 관측소 상세 (주소, 위도, 경도 등)
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RfObservatoryDto{

    private String rfobscd; // 관측소 코드

    private String type; // 관측소 타입 - rf(강수량 관측소), wl(수위 관측소), flw(유량 관측소)

    private String addr; // 주소

    private String etcaddr; // 나머지 주소

    private String lon; // 경도

    private String lat; // 위도

}

