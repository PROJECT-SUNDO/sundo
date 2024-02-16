package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.WaterLevelFlow;
import org.sundo.wamis.entities.WaterLevelFlowId;

/**
 * 수위 + 유량
 */

public interface WaterLevelFlowRepository extends JpaRepository<WaterLevelFlow, WaterLevelFlowId>,
        QuerydslPredicateExecutor<WaterLevelFlow> {
}
