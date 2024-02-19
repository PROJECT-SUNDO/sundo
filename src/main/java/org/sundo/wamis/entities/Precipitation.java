package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class Precipitation {
    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 10)
    private String rfobscd;//강수량 관측소

    @Column(length = 12)
    private String ymdhm;////년월일시분

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDate ymd;//년월일

    @Column(length = 10)
    private double rf; // 강수량

}
