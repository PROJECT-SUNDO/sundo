package org.sundo.wamis.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.ObservatoryId;

public interface ObservatoryRepository extends JpaRepository<Observatory, ObservatoryId>, QuerydslPredicateExecutor<Observatory> {
}
