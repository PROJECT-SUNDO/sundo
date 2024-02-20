package org.sundo.wamis.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name="OBSERVATION")
@DiscriminatorColumn(name = "TYPE")
public abstract class Observation {

    @Id
    @GeneratedValue
    private Long seq;

    @Column(length = 12)
    @JsonFormat(pattern="yyyyMMddHHmm")
    private String ymdhm;////년월일시분

    //@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDate ymd;//년월일

}
