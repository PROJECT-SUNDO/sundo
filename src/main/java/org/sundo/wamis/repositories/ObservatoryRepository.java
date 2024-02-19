package org.sundo.wamis.repositories;


import com.querydsl.core.BooleanBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.sundo.wamis.entities.Observatory;
import org.sundo.wamis.entities.ObservatoryId;
import org.sundo.wamis.entities.QObservatory;

import java.util.List;
import java.util.Optional;
import org.sundo.wamis.entities.QObservatory;

import java.util.Optional;

public interface ObservatoryRepository extends JpaRepository<Observatory, ObservatoryId>, QuerydslPredicateExecutor<Observatory> {


    default Optional<Observatory> getOne(String obscd, String type){
        QObservatory observatory = QObservatory.observatory;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(observatory.obscd.eq(obscd));
        builder.and(observatory.type.eq(type));

        return findOne(builder);
    }

    Optional<ObservatoryRepository> findByObsnm(String obsnm); //optional형태의 반환

    Optional<ObservatoryRepository> findBySbsncd(String sbcd); //

    default boolean existsByObsnm(String obsnm) {
        return exists(QObservatory.observatory.obsnm.eq(obsnm));
    }

    default boolean existsBySbsncd(String sbsncd) {
        return exists(QObservatory.observatory.sbsncd.eq(sbsncd));
    }
}
