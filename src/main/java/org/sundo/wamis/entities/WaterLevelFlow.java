package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * 수위 + 유량 데이터
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@DiscriminatorValue("wlf")
public class WaterLevelFlow extends Observation {
    @Column(length = 10)
    private String wlobscd;

    @Column(length = 10)
    private double wl; // 수위

    @Column(length = 10)
    private double fw; // 유량



}
