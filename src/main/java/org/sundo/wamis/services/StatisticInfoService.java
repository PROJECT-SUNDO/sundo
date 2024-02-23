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
import org.sundo.list.controllers.StatisticSearch;
import org.sundo.wamis.entities.QStatistic;
import org.sundo.wamis.entities.Statistic;
import org.sundo.wamis.repositories.StatisticRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static org.springframework.data.domain.Sort.Order.desc;

/**
 * 관측 정보
 */

@Service
@RequiredArgsConstructor
public class StatisticInfoService {
    private final StatisticRepository statisticRepository;
    private final HttpServletRequest request;


    public ListData<Statistic> getStatisticList(StatisticSearch search){
        int page = search.getPage();
        int limit = search.getLimit();

        LocalDate sdate = search.getSdate();
        LocalDate edate = search.getEdate();
        String obscd = search.getObscd().trim();
        String type = search.getType();

        QStatistic statistic = QStatistic.statistic;
        BooleanBuilder andbuilder = new BooleanBuilder();

        if(StringUtils.hasText(obscd)) {
            andbuilder.and(statistic.obscd.eq(obscd));
        }
        if(StringUtils.hasText(type)) {
            andbuilder.and(statistic.type.eq(type));
        }
        if(sdate != null) {
            andbuilder.and(statistic.date.goe(sdate)); // >=
        }
        if(edate != null) {
            andbuilder.and(statistic.date.loe(edate)); // <=
        }

        /* 페이징 처리 S */
        // PageRequest.of(페이지 번호(0부터 시작), 페이지당 데이터의 수)
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("date")));

        Page<Statistic> data = statisticRepository.findAll(andbuilder, pageable);
        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);
        /* 페이징 처리 E */

        return new ListData<>(data.getContent(), pagination);
    }
}
