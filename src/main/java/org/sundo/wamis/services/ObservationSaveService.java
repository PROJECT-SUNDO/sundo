package org.sundo.wamis.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sundo.list.controllers.RequestObservation;
import org.sundo.wamis.entities.Observation;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.repositories.ObservationRepository;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

@Service
@RequiredArgsConstructor
public class ObservationSaveService {
    private final PrecipitationRepository precipitationRepository; // 강수량
    private final WaterLevelFlowRepository waterLevelFlowRepository; // 유량, 수위


    /**
     * 관측값 수정
     * @param form
     */
    public void edit(RequestObservation form){
        String type = form.getType();
        Long seq = form.getSeq();
        if(type.equals("rf")){

            Precipitation precipitation = precipitationRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);
            precipitation.setRf(form.getRf());

            precipitationRepository.saveAndFlush(precipitation);
        }else{
            WaterLevelFlow waterLevelFlow = waterLevelFlowRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);

            if(type.equals("flw")){
                waterLevelFlow.setFw(form.getFw());
            }else{
                waterLevelFlow.setWl(form.getWl());
            }
            waterLevelFlowRepository.saveAndFlush(waterLevelFlow);
        }

    }
}
