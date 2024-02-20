package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sundo.list.controllers.RequestObservatory;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.persistence.Transient;

@Service
@Transactional
@RequiredArgsConstructor
public class ObservatoryInfoService {
    private final ObservatoryRepository observatoryRepository;

    public RequestObservatory getRequest(String obscd, String type){
        Observatory obs = observatoryRepository.getOne(obscd, type).orElse(null);

        String obsnm = "";
        boolean clsyn = false;

        if(obs != null){
            obsnm = obs.getObsnm();
            clsyn = "운영".equals(obs.getClsyn());
        }

        RequestObservatory form = RequestObservatory.builder()
                .obscd(obscd)
                .obsnm(obsnm)
                .clsyn(clsyn)
                .type(type)
                .build();

        return form;
    }

}
