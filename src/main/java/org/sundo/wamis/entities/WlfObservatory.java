/*
package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

*/
/**
 * 수위 + 유량 관측소
 *//*

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class WlfObservatory {
    @Id
    @Column(length = 10)
    private String wlobscd; // 관측소 코드

    @Column
    private boolean type; // 수위+유량 모두 존재 -> 1, 수위만 존재 -> 0

    @Column(length = 40)
    private String obsnm; // 관측소명

    @Column(length = 40)
    private String agcnm; // 관할기관

    @Column(length = 80)
    private String addr; // 주소

    @Column(length = 80)
    private String etcAddr; // 나머지 주소

    @Column(length = 20)
    private String lon; // 경도

    @Column(length = 20)
    private String lat; // 위도

    // 필요 없을 듯

    @Column(length = 20)
    private String gdt; // 영점표고 : 어떤 기준면에서부터 양수표 0점까지 이르는 높이.

    @Column(length = 20)
    private double attwl; // 관심 수위

    @Column(length = 20)
    private String wrnwl; // 주의보 수위

    @Column(length = 20)
    private String almwl; // 경보 수위

    @Column(length = 20)
    private String srswl; // 심각 수위

    @Column(length = 20)
    private String pfh; // 계획홍수위

    @Column(length = 20)
    private String fstnyn; // 특보지점여부

    // api에 없지만 필요
*/
/*    @Column(length=40)
    private String obsknd; // 관측방법*//*


    // 사용 여부
    @Transient
    private boolean active;


}

*/
