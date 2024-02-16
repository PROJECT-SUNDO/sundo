package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.RfObservatory;
import org.sundo.wamis.entities.WlObservatory;

public interface WlObservatoryRepository extends JpaRepository<WlObservatory, String>,
        QuerydslPredicateExecutor<WlObservatory> {
}
