package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.Generated;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(WaterLevelId.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class WaterLevel{
    @Id
    private String obscd;

    @Id
    @JsonFormat(pattern="yyyyMMddHH")
    private LocalDateTime ymdh;

    private double wl;
}
