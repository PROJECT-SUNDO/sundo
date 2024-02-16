package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.RfObservatory;

public interface RfObservatoryRepository extends JpaRepository<RfObservatory, String>,
        QuerydslPredicateExecutor<RfObservatory> {
}
