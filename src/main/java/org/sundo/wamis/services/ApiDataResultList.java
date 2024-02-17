package org.sundo.wamis.services;

import lombok.Data;

import java.util.List;

@Data
public class ApiDataResultList<T> {
    private Object[] links;
    private List<T> content;
}
