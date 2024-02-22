package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sundo.commons.entities.Base;

import javax.persistence.*;
import java.io.Serializable;

/**
 *강수량, 수위, 유량 관측소
 */
@Data
@Entity @Builder
@IdClass(ObservatoryId.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class Observatory extends Base implements Serializable {
    @Id
    @Column(length=10)
    private String obscd; // 관측소 코드

    @Id
    @Column(length=3)
    private String type; // 관측소 타입 - rf(강수량 관측소), wl(수위 관측소), flw(유량 관측소)

    @Column(length=40)
    private String bbsnnm; // 대권역명
    @Column(length=40)
    private String obsnm; // 관측소명
    @Column(length=40)
    private String clsyn; // 운영여부
    @Column(length=40)
    private String obsknd; // 관측방법
    @Column(length=40)
    private String sbsncd; // 표준유역코드
    @Column(length=40)
    private String mngorg; // 관할기관

    // 관측소 상세
    @Column(length = 10)
    private String lon; // 경도
    @Column(length = 10)
    private String lat; // 위도
    @Column(length = 80)
    private String addr;
    @Column(length = 80)
    private String etcaddr;

    private String attwl; // 관심 수위

    private String wrnwl; // 주의보 수위

    private String almwl; // 경보 수위

    private String srswl; // 심각 수위

    private String pfh; // 계획홍수위

    private String fstnyn; // 특보지점여부

    private String cctvUrlH; // 고화질 주소
    private String cctvUrlL; // 저화질 주소

    private boolean useOutlier = true; // 이상치 사용여부
    private double outlier; // 이상치

    @Transient
    private double data; // 최근 10분 데이터
}
