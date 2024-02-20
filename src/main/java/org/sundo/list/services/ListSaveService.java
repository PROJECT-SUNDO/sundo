package org.sundo.list.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.sundo.list.controllers.RequestObservatory;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.ObservatoryId;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ListSaveService {
    private final ObservatoryRepository observatoryRepository;
    private final HttpServletRequest request;

    public Observatory save(RequestObservatory form) {

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";

        String obscd = form.getObscd();

        String obstype = form.getType();

        Observatory data = null;

        if (StringUtils.hasText(obscd) && StringUtils.hasText(type) && mode.equals("update")) { // 글 수정
            ObservatoryId id = new ObservatoryId(obscd, type);

            data = observatoryRepository.findById(id).orElseThrow(ObservatoryDataNotFoundException::new);
        } else { // 글 작성
            /**
             * 관측소 코드 및 관측소 타입을 복합키로 PK로 지정하였고, 기본키 이므로 최초 추가시에만
             * 데이터 설정
             */

            data = new Observatory();
            data.setObsnm(form.getObsnm());
            data.setObscd(form.getObscd());
            data.setSbsncd(form.getSbsncd());
            data.setType(form.getType());

        }

        data.setObsknd(form.getObsknd());
        data.setLatitude(form.getLatitude());
        data.setLongitude(form.getLongitude());
        data.setMngorg(form.getMngorg());
        data.setObsnm(form.getObsnm());
        data.setSbsncd(form.getSbsncd());
        data.setType(form.getType());

        data = observatoryRepository.saveAndFlush(data);
        return data;
    }
    /* List화가 꼭 필요한가???

    public void saveList(List<Integer> chks) {

        }

     */
}