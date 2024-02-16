package org.sundo.list.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ListInfoService {

    private final ObservatoryRepository observatoryRepository;
    private final HttpServletRequest request;

}
