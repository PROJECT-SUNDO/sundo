package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.WlfObservatory;

public interface WlfObservatoryRepository extends JpaRepository<WlfObservatory, String>,
        QuerydslPredicateExecutor<WlfObservatory> {
}
