package org.sundo.list.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.sundo.common.ListData;
import org.sundo.common.Pagination;
import org.sundo.common.Utils;
import org.sundo.list.controllers.ListDataSearch;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.QObservatory;
import org.sundo.wamis.repositories.ObservatoryRepository;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class ListInfoService {

    private final ObservatoryRepository observatoryRepository;
    private final HttpServletRequest request;

    /**
     * 목록 조회
     * @param search
     * @return
     */
    public ListData<Observatory> getList(ListDataSearch search) {

        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 10);
        int offset = (page -1) * limit;

        QObservatory observatory = QObservatory.observatory;
        BooleanBuilder andBuilder = new BooleanBuilder();

    //    PathBuilder<Observatory> pathBuilder = new PathBuilder<>(Observatory.class, "observatory");
    /*
        List<Observatory> items = new JPAQueryFactory(em)
                .selectFrom(observatory)
//                .leftJoin(observatory.obscd) /* ontoMany 이런게 없다.
                .fetchJoin()
                .offset(offset)
                .limit(limit)
                .where(andBuilder)
                .orderBy(new OrderSpecifier(Order.DESC, pathBuilder.get("seq")))
                .fetch();
    */


        /* 페이징 처리 S */
        Pageable pageable = PageRequest.of(page - 1, limit);

        Page<Observatory> data = observatoryRepository.findAll(andBuilder, pageable);

        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);
        /* 페이징 처리 E */

        return new ListData<>(data.getContent(), pagination);
    }
}
