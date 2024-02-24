package org.sundo.wamis.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.sundo.commons.ListData;
import org.sundo.commons.Pagination;
import org.sundo.list.controllers.ObservationSearch;
import org.sundo.wamis.entities.*;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.repositories.WaterLevelFlowRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Order.desc;

// 관측값 인포 서비스

@Service
@RequiredArgsConstructor
public class ObservationInfoService {
    private final PrecipitationRepository precipitationRepository; // 강수량
    private final WaterLevelFlowRepository waterLevelFlowRepository; //수위 + 유량
    private final EntityManager em;
    private final HttpServletRequest request;


    public ListData<Precipitation> getRFList(ObservationSearch search){
        int page = search.getPage();
        int limit = search.getLimit();

        LocalDate sdate = search.getSdate();
        LocalDate edate = search.getEdate();
        String obscd = search.getObscd().trim();

        QPrecipitation precipitation = QPrecipitation.precipitation;
        BooleanBuilder andbuilder = new BooleanBuilder();

        if(search.isOut()){
            double outlier = search.getOutlier();
            andbuilder.and(precipitation.rf.goe(outlier));
        }

        if(StringUtils.hasText(obscd)){
            andbuilder.and(precipitation.rfobscd.eq(obscd));
        }
        // 날짜 검색
        if (sdate != null){
            andbuilder.and(precipitation.ymd.goe(sdate));
        }
        if(edate != null){
            andbuilder.and(precipitation.ymd.loe(edate));
        }

        /* 페이징 처리 S */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("ymdhm")));

        // 단일 테이블 불러올때
        Page<Precipitation> data = precipitationRepository.findAll(andbuilder, pageable);

        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);

        /* 페이징 처리 E */

        return new ListData<>(data.getContent(), pagination);

    }

    public ListData<WaterLevelFlow> getWLFList(ObservationSearch search){
        int page = search.getPage();
        int limit = search.getLimit();

        LocalDate sdate = search.getSdate();
        LocalDate edate = search.getEdate();
        String type = search.getType();
        String obscd = search.getObscd().trim();

        // 수위, 유량일 경우
        QWaterLevelFlow waterLevelFlow = QWaterLevelFlow.waterLevelFlow;
        BooleanBuilder andBuilder = new BooleanBuilder();

        // 날짜 검색
        if (sdate != null){
            andBuilder.and(waterLevelFlow.ymd.goe(sdate));
        }
        if(edate != null){
            andBuilder.and(waterLevelFlow.ymd.loe(edate));
        }

        // 수위
        if("wl".equals(type)){
            if(search.isOut()){
                double outlier = search.getOutlier();
                andBuilder.and(waterLevelFlow.wl.goe(outlier));
            }
        }else{
            // 유량
            if(search.isOut()){
                double outlier = search.getOutlier();
                andBuilder.and(waterLevelFlow.fw.goe(outlier));
            }
        }
        if(StringUtils.hasText(obscd)){
            andBuilder.and(waterLevelFlow.wlobscd.eq(obscd));
        }

        /* 페이징 처리 S */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("ymdhm")));

        // 단일 테이블 불러올때
        Page<WaterLevelFlow> data = waterLevelFlowRepository.findAll(andBuilder, pageable);

        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);

        /* 페이징 처리 E */

        return new ListData<>(data.getContent(), pagination);
    }

    /**
     * 강수량
     * @param seq
     * @return
     */
    public Precipitation getPre(Long seq){
        Precipitation precipitation = precipitationRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);

        return precipitation;
    }

    /**
     * 수위/유량 데이터
     * @param seq
     * @return
     */
    public WaterLevelFlow getWLF(Long seq){
        WaterLevelFlow waterLevelFlow = waterLevelFlowRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);

        return waterLevelFlow;
    }


}
