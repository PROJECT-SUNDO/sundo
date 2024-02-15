package org.sundo.list.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.list.entities.Observatory;

public interface ObservatoryRepository extends JpaRepository<Observatory,String>, QuerydslPredicateExecutor<Observatory> {

}
