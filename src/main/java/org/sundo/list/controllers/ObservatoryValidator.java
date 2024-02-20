package org.sundo.list.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.sundo.wamis.repositories.ObservatoryRepository;

@Component
@RequiredArgsConstructor
public class ObservatoryValidator implements Validator {

    private final ObservatoryRepository observatoryRepository;

    /**
     * 관측소 등록시 유효성 검사
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {

        return clazz.isAssignableFrom(RequestObservatory.class);
    }

    @Override
    public void validate(Object target, Errors errors) {


        RequestObservatory form = (RequestObservatory) target;
        String obsnm = form.getObsnm();//관측소 이름
        String obscd = form.getObscd();// 표준 유역 코드

        //중복 여부체크

        if (observatoryRepository.existsByObsnm(obsnm)){
            errors.rejectValue("obsnm", "Duplicated");

        }

        if (observatoryRepository.existsByObscd(obscd)){
            errors.rejectValue("obscd", "Duplicated");
        }

    }
}

