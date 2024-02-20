package org.sundo.list.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.sundo.list.controllers.RequestObservatory;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class ListInfoService {
    private final ObservatoryRepository observatoryRepository;
    private HttpServletRequest request;

    /*
     * 게시판 설정 조회
     * 없으면 예외를 던짐.
    */
    public Observatory get(String obscd) {
        Observatory data = observatoryRepository.findByobscd(obscd).orElseThrow(ObservatoryDataNotFoundException::new);//게시글 가져오기

        //addBoardInfo(board);//추가 정보 더 넣기

        return data;

    }

    public RequestObservatory getForm(String obscd){
        Observatory data = get(obscd);

        RequestObservatory form = new ModelMapper().map(data,RequestObservatory.class);
        return form;
    }

}
