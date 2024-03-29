package org.sundo.list.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ObservatorySettingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestObservatory.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // 이상치는 항상 양수
        RequestObservatory form = (RequestObservatory) target;

        double outlier = 0;

        if(!form.getOutlier().isNaN()){
            outlier = form.getOutlier();
        }

        if(outlier < 0){
            errors.rejectValue("outlier", "NotPositive");
        }
    }
}
