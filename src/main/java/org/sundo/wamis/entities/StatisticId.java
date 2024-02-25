package org.sundo.wamis.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class StatisticId implements Serializable {
    private String obscd;
    private String type;
    private LocalDate date;

}
