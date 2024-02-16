package org.sundo.wamis.services;

import lombok.Data;

import java.util.List;

@Data
public class ApiResultList<T> {
    private Object[] links;
    private List<T> content;
}
