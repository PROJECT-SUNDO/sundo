package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.FlwObservatory;
import org.sundo.wamis.entities.RfObservatory;

public interface FlwObservatoryRepository extends JpaRepository<FlwObservatory, String>,
        QuerydslPredicateExecutor<FlwObservatory> {
}
