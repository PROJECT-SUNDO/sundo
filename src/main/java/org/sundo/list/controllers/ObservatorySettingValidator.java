package org.sundo.list.controllers;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ObservatorySettingValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestObservatory.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // 이상치는 항상 양수
        RequestObservatory form = (RequestObservatory) target;
        double outlier = form.getOutlier();

        if(outlier < 0){
            errors.rejectValue("outlier", "NotPositive");
        }
    }
}
