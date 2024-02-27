package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sundo.wamis.entities.Observation;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.repositories.ObservationRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

@Service
@RequiredArgsConstructor
public class ObservationDeleteService {

    private final PrecipitationRepository precipitationRepository; // 강수량
    private final WaterLevelFlowRepository waterLevelFlowRepository; // 유량, 수위


    public void delete(Long seq, String type){
        if("rf".equals(type)){
            Precipitation precipitation = precipitationRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);
            precipitationRepository.delete(precipitation);
            precipitationRepository.flush();
        }else{
            WaterLevelFlow waterLevelFlow = waterLevelFlowRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);
            waterLevelFlowRepository.delete(waterLevelFlow);
            waterLevelFlowRepository.flush();
        }

    }
}
