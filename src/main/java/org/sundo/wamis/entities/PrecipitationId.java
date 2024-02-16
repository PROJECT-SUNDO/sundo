package org.sundo.wamis.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PrecipitationId implements Serializable {
    private String obscd;
    private LocalDateTime ymdh;
}
