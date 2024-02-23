package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Observation;

public interface ObservationRepository extends JpaRepository<Observation, Long>, QuerydslPredicateExecutor<Observation> {
}
