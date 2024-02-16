package org.sundo.wamis.services;

import lombok.Data;

import java.util.List;

@Data
public class ApiResultList<T> {
    private ApiResult result;
    private int count;
    private List<T> list;
}
