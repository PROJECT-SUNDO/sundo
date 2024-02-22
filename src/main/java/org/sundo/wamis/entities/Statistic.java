package org.sundo.wamis.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.time.LocalDate;

@Entity @Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@IdClass(StatisticId.class)
public class Statistic implements Serializable {
    @Id
    private String obscd;
    @Id
    private String type;
    @Id
    private LocalDate date;

    private double avg0;
    private double acc0;

    private double avg1;
    private double acc1;

    private double avg2;
    private double acc2;

    private double avg3;
    private double acc3;

    private double avg4;
    private double acc4;

    private double avg5;
    private double acc5;

    private double avg6;
    private double acc6;

    private double avg7;
    private double acc7;

    private double avg8;
    private double acc8;

    private double avg9;
    private double acc9;

    private double avg10;
    private double acc10;

    private double avg11;
    private double acc11;

    private double avg12;
    private double acc12;

    private double avg13;
    private double acc13;

    private double avg14;
    private double acc14;

    private double avg15;
    private double acc15;

    private double avg16;
    private double acc16;

    private double avg17;
    private double acc17;

    private double avg18;
    private double acc18;

    private double avg19;
    private double acc19;

    private double avg20;
    private double acc20;

    private double avg21;
    private double acc21;

    private double avg22;
    private double acc22;

    private double avg23;
    private double acc23;

    private String data;


}
