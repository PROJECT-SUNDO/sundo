package org.sundo.dashboard.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sundo.commons.ListData;
import org.sundo.commons.Pagination;
import org.sundo.commons.Utils;
import org.sundo.list.controllers.ObservatorySearch;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.QObservatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardInfoService {

    private final ObservatoryRepository observatoryRepository;
    private final HttpServletRequest request;


    public ListData<Observatory> getRFList(ObservatorySearch search) {

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 10);

        QObservatory observatory = QObservatory.observatory;
        BooleanBuilder andBuilder = new BooleanBuilder();

        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Observatory> data = observatoryRepository.findAll(andBuilder, pageable);

        List<Observatory> items = data.getContent();
        System.out.println("아아아아" + items);

        String type = search.getType();
        andBuilder.and(observatory.type.eq("rf"));

        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);

        return new ListData<>(items, pagination);
    }
}
