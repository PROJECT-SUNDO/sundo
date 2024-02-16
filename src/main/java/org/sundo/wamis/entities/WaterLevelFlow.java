package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 수위 + 유량
 */
@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
//@IdClass(WaterLevelFlowId.class)
public class WaterLevelFlow {
    @Id @GeneratedValue
    private Long seq;

    @Column(length = 10)
    private String wlobscd;

    @Column(length = 12)
    private String ymdhm;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDate ymd;

    @Column(length = 10)
    private double wl; // 수위

    @Column(length = 10)
    private String fw; // 유량



}
