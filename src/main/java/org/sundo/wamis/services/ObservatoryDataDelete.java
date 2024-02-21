package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sundo.commons.Utils;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ObservatoryDataDelete {
    private final ObservatoryRepository observatoryRepository;
    private final ObservatoryInfoService observatoryInfoService;
    private final Utils utils;

    public void delete(String obscd) {
        Observatory observatory = observatoryInfoService.get(obscd);
        observatoryRepository.delete(observatory);
        observatoryRepository.flush();

    }

}
