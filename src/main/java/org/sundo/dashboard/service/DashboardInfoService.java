package org.sundo.dashboard.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.QObservatory;
import org.sundo.wamis.repositories.ObservatoryRepository;
import org.sundo.wamis.repositories.PrecipitationRepository;
import org.sundo.wamis.services.ObservationNotFoundException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DashboardInfoService {

    private final ObservatoryRepository observatoryRepository;
    private final PrecipitationRepository precipitationRepository; // 강수량
    private final HttpServletRequest request;
    private final EntityManager em;


    public ListData<Observatory> getRFList(ObservatorySearch search) {

        // 페이지 및 제한 값을 가져오고 음수인 경우 기본값으로 설정합니다.
        int page = Utils.onlyPositiveNumber(search.getPage(), 1);
        int limit = Utils.onlyPositiveNumber(search.getLimit(), 10);
        int offset = (page - 1) * limit; // 레코드 시작 위치

        // QueryDSL을 사용하여 쿼리를 생성하기 위한 빌더 생성
        QObservatory observatory = QObservatory.observatory;

        // Spring Data JPA의 Pageable 객체를 생성하여 페이지 및 제한을 설정
        Pageable pageable = PageRequest.of(page - 1, limit);

        PathBuilder<Observatory> pathBuilder = new PathBuilder<>(Observatory.class, "observatory");
        List<Observatory> items = new JPAQueryFactory(em)
                .selectFrom(observatory)
                .offset(offset)
                .limit(limit)
                .where(observatory.type.eq("rf"))
                .fetch();

        // 단일 테이블 불러올때
        Page<Observatory> data = observatoryRepository.findAll(observatory.type.eq("rf"), pageable);

        // 페이징 정보를 기반으로 Pagination 객체 생성
        Pagination pagination = new Pagination(page, (int) data.getTotalElements(), 10, limit, request);

        // ListData 객체를 생성하여 결과 반환
        return new ListData<>(items, pagination);
    }

    public Precipitation getPre(Long seq){
        Precipitation precipitation = precipitationRepository.findById(seq).orElseThrow(ObservationNotFoundException::new);

        return precipitation;
    }
}
