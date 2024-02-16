package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(WaterFlowLevelId.class)
public class WaterFlowLevel{
    @Id
    @Column(length = 10)
    private String obscd;

    @Id
    private LocalDateTime ymdhi;

    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate ymd;

    @Column(length = 10)
    private String flw;


}
