package org.sundo.wamis.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.sundo.commons.ListData;
import org.sundo.commons.Pagination;
import org.sundo.commons.Utils;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.list.controllers.RequestObservatory;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.QObservatory;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Service
@Transactional
@RequiredArgsConstructor
public class ObservatoryInfoService {
    private final ObservatoryRepository observatoryRepository;
    private final HttpServletRequest request;
    private final EntityManager em;
    private final PrecipitationRepository precipitationRepository;
    private final WaterLevelFlowRepository waterLevelFlowRepository;

    public Observatory get(String obscd) {
        //Observatory data = observatoryRepository.findByObscd(obscd).orElseThrow(ObservationNotFoundException::new);
        QObservatory observatory = QObservatory.observatory;
        List<Observatory> items = (List<Observatory>)observatoryRepository.findAll(observatory.obscd.eq(obscd));

        return items == null || items.isEmpty() ? null : items.get(0);
    }

    public RequestObservatory getRequest(String obscd, String type){
        Observatory obs = observatoryRepository.getOne(obscd, type).orElse(null);

        String obsnm = "";
        boolean clsyn = false;

        if(obs != null){
            obsnm = obs.getObsnm();
            clsyn = "운영".equals(obs.getClsyn());
        }
        double outlier = obs.getOutlier();

        if("wl".equals(type)){
            try{
                outlier = Double.parseDouble(obs.getWrnwl().toString());
            }catch (Exception e){
                outlier = 0;
            }
        }

        RequestObservatory form = RequestObservatory.builder()
                .mode("update")
                .obscd(obscd)
                .obsnm(obsnm)
                .clsyn(clsyn)
                .type(type)
                .useOutlier(obs.isUseOutlier())
                .outlier(outlier)
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
            if(type.equals("cctv")){
                andBuilder.and(observatory.cctvUrlH.isNotEmpty());
                andBuilder.and(observatory.cctvUrlL.isNotEmpty());
            }else if (!type.equals("ALL")) {
                andBuilder.and(observatory.type.eq(type));
                if(type.equals("flw")){
                    andBuilder.and(observatory.clsyn.isNull());
                }else{
                    andBuilder.and(observatory.clsyn.eq("운영"));
                }
            }
        }

        /* 검색 조건 E */
        /* 정렬 조건 S */
        String order = search.getOrder();
        Sort sort = Sort.by(asc("obscd"));
        if (StringUtils.hasText(order)) {
            if (order.equals("obsnm")) {
                sort = Sort.by(asc("obsnm"));
            } else if (order.equals("rf")) {
                sort = Sort.by(desc("rf"));
            } else if (order.equals("wl")) {
                sort = Sort.by(desc("wl"));
            } else if (order.equals("flw")) {
                sort = Sort.by(desc("fw"));
            } else if (order.equals("upstream")) {
                sort = Sort.by(desc("lon"));
            } else if (order.equals("downstream")){
                sort = Sort.by(asc("lon"));
            }
        }

        /* 정렬 조건 E */
        /* 페이징 처리 S */


        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        // 단일 테이블 불러올때
        Page<Observatory> data = observatoryRepository.findAll(andBuilder, pageable);

        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);

        /* 페이징 처리 E */

        List<Observatory> items = data.getContent();
        items.forEach(this::addInfo);

        return new ListData<>(items, pagination);
    }


    private void addInfo(Observatory observatory){
        String type = observatory.getType();
        double data = 0;

        if("rf".equals(type)){
            data = observatory.getRf();
            observatory.setData(data);

        }else if ("wl".equals(type)){
            data = observatory.getWl();
            observatory.setData(data);
        }else if("flw".equals(type)){
            data = observatory.getFw();
            observatory.setData(data);
        }

    }




}
