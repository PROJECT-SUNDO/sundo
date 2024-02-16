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
public class RfObservatory {
    @Id
    @Column(length = 10)
    private String rfobscd; // 관측소 코드

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

    // 사용 여부
    @Transient
    private boolean active;


    // api에 없지만 필요
/*    @Column(length=40)
    private String obsknd; // 관측방법*/


}

