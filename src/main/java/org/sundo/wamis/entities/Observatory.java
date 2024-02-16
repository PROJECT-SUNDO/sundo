package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity
@IdClass(ObservatoryId.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Observatory {

    @Id
    @Column(length = 10)
    private String obscd; // 관측소 코드

    @Id
    @Column(length = 3)
    private String type; // 관측소 타입 - rf(강수량 관측소), wl(수위 관측소), flw(유량)

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

}

