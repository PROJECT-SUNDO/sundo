package org.sundo.wamis.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.sundo.commons.ListData;
import org.sundo.commons.Pagination;
import org.sundo.commons.Utils;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.list.controllers.RequestObservatory;
import org.sundo.list.services.ObservatoryDataNotFoundException;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.QObservatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ObservatoryInfoService {
    private final ObservatoryRepository observatoryRepository;
    private final HttpServletRequest request;
    private final EntityManager em;

    public Observatory get(String obscd) {
        Observatory data = observatoryRepository.findByObscd(obscd).orElseThrow(ObservatoryDataNotFoundException::new);

        return data;
    }

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
                .outlier(obs.getOutlier())
                .build();

        return form;
    }

    /**
     * 목록 조회
     *
     * @param search
     * @return
     */
    public ListData<Observatory> getList(ObservatorySearch search) {

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 10);
        int offset = (page -1) * limit;

        QObservatory observatory = QObservatory.observatory;
        BooleanBuilder andBuilder = new BooleanBuilder();

        /* 검색 조건 S */
        String obscd = search.getObscd();
        String obsnm = search.getObsnm();
        String type = search.getType();

        if (StringUtils.hasText(obscd)) {

            obscd = obscd.trim();

            andBuilder.and(observatory.obscd.contains(obscd));

        }

        if (StringUtils.hasText(obsnm)) {

            obsnm = obsnm.trim();

            andBuilder.and(observatory.obsnm.contains(obsnm));

        }


        if (StringUtils.hasText(type)) {

            if (!type.equals("ALL")) {
                andBuilder.and(observatory.type.eq(type));
            }
        }

        /* 검색 조건 E */

        /* 페이징 처리 S */

        Pageable pageable = PageRequest.of(page - 1, limit);

        // 단일 테이블 불러올때
        Page<Observatory> data = observatoryRepository.findAll(andBuilder, pageable);
        System.out.println("아아아아"+ data);

        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);

        /* 페이징 처리 E */

        List<Observatory> items = data.getContent();

        return new ListData<>(items, pagination);
    }

}
