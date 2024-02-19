package org.sundo.wamis.repositories;


import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.ObservatoryId;
import org.sundo.wamis.entities.QObservatory;

import java.util.List;
import java.util.Optional;

public interface ObservatoryRepository extends JpaRepository<Observatory, ObservatoryId>, QuerydslPredicateExecutor<Observatory> {


    default Optional<Observatory> getOne(String obscd, String type){
        QObservatory observatory = QObservatory.observatory;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(observatory.obscd.eq(obscd));
        builder.and(observatory.type.eq(type));

        return findOne(builder);
    }
}
