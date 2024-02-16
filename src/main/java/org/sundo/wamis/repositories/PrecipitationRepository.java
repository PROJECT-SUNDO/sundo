package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Precipitation;
import org.sundo.wamis.entities.PrecipitationId;

public interface PrecipitationRepository extends JpaRepository<Precipitation, PrecipitationId>, QuerydslPredicateExecutor<Precipitation> {
}
