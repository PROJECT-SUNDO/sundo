package org.sundo.wamis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.WaterLevel;
import org.sundo.wamis.entities.WaterLevelId;

public interface WaterLevelRepository extends JpaRepository<WaterLevel, WaterLevelId>,
        QuerydslPredicateExecutor<WaterLevel> {


}
