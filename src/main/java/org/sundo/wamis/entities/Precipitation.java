package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(PrecipitationId.class)
@JsonIgnoreProperties(ignoreUnknown = true) //JSON -> 자바 객체 변환시 없는 필드 무시
public class Precipitation {

    @Id
    @Column(length = 10)
    private String obscd;//관측소 코드

    @Id
    @JsonFormat(pattern = "yyyyMMddHH")
    private LocalDateTime ymdh; // 관측 일시

    private double rf; //강수량

}
