package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 강수량 관측소
 */


@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlwObservatory {
    @Id
    @Column(length=10)
    private String obscd; // 관측소 코드

    @Column(length=40)
    private String obsnm; // 관측소명

    @Column(length=40)
    private String bbsnnm; // 대권역명

    @Column(length = 40)
    private String obstype; // enum

    @Column(length=40)
    private String sbsncd; // 표준유역코드

    @Column(length=40)
    private String mngorg; // 관할기관

    @Column(length=40)
    private String minyear; // 자료보유기간-시작

    @Column(length=40)
    private String maxyear; // 자료보유기간-종료

    // 10분
    @Column(length = 20)
    private double tenAvg;

    // 사용 여부
    @Transient
    private boolean active;

}

