package org.sundo.list.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.sundo.wamis.repositories.ObservatoryRepository;

@Component
@RequiredArgsConstructor
public class ObservatoryValidator implements Validator, RegisterValidator {

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
        String obsnm = form.getObsnm(); //관측소명
        String obscd = form.getObscd();// 관측소 코드
        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode : "write";
        //중복 여부체크

        if (mode.equals("write") && observatoryRepository.existsByObsnm(obsnm)){
            errors.rejectValue("obsnm", "Duplicated");

        }

        if (mode.equals("write") && observatoryRepository.existsByObscd(obscd)){
            errors.rejectValue("obscd", "Duplicated");
        }

        if (mode.equals("write")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "obscd", "NotBlank");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "obsnm", "NotBlank");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "NotBlank");

            if (obscd.length() != 8) {
                errors.rejectValue("obscd", "Size");
            }
        }



        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "latitude", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "longitude", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "obsknd", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mngorg", "NotBlank");
    }
}

