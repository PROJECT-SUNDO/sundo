package org.sundo.list.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ListDeleteService {

    private final ObservatoryRepository observatoryRepository;
    private final ListInfoService listInfoService;
    public Long delete(Long seq) {
        Observatory data = listInfoService.get(seq);
        observatoryRepository.delete(data);
        observatoryRepository.flush();

    }



}
