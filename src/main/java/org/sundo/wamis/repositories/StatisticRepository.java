package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Statistic;
import org.sundo.wamis.entities.StatisticId;

public interface StatisticRepository extends JpaRepository<Statistic, StatisticId>,
        QuerydslPredicateExecutor<Statistic> {
}
