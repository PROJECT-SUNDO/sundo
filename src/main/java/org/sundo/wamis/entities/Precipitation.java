package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

/**
 * 강수량 데이터
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@DiscriminatorValue("rf")
public class Precipitation extends Observation {


    @Column(length = 10)
    private String rfobscd;//강수량 관측소

    @Column(length = 10)
    private double rf; // 강수량

}
