package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Precipitation;

import java.util.List;
import java.util.Optional;

/**
 * 강수량 데이터
 */
public interface PrecipitationRepository extends JpaRepository<Precipitation, Long>,
        QuerydslPredicateExecutor<Precipitation> {

    Optional<List<Precipitation>> findByRfobscdOrderByYmdDesc(String rfobscd);
}
