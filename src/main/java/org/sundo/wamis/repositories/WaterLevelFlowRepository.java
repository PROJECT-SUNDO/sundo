
package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.WaterLevelFlow;

import java.util.List;
import java.util.Optional;


/**
 * 수위 + 유량
 */


public interface WaterLevelFlowRepository extends JpaRepository<WaterLevelFlow, Long>,
        QuerydslPredicateExecutor<WaterLevelFlow> {
    Optional<List<WaterLevelFlow>> findByWlobscdOrderByYmdDesc(String wlobscd);
}

