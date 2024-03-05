package org.sundo.list.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.sundo.wamis.repositories.ObservatoryRepository;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        String obsnm = form.getObsnm(); //관측소명
        String obscd = form.getObscd();// 관측소 코드
        String latitude = form.getLatitudes() == null ? null : Arrays.stream(form.getLatitudes()).collect(Collectors.joining("-"));
        String longitude = form.getLongitudes() == null ? null : Arrays.stream(form.getLongitudes()).collect(Collectors.joining("-"));

        form.setLatitude(latitude);
        form.setLongitude(longitude);

        String mode = form.getMode();


        mode = StringUtils.hasText(mode) ? mode : "write";

        //관측소 이름, 관측소 코드 중복 여부체크
        if (mode.equals("write") && observatoryRepository.existsByObsnm(obsnm)){
            errors.rejectValue("obsnm", "Duplicated");

        }

        if (mode.equals("write") && observatoryRepository.existsByObscd(obscd)){
            errors.rejectValue("obscd", "Duplicated");
        }

        // 2. 표준 코드 복잡성체크
        if(StringUtils.hasText(obscd) && Pattern.compile("\\D").matcher(obscd).find()){
            errors.rejectValue("obscd","Format");
        }

        //3.경도, 위도 코드 복잡성체크
        if(StringUtils.hasText(latitude) && (Pattern.compile("[^0-9\\-]").matcher(latitude).find() || latitude.replace("-", "").trim().isEmpty() || latitude.length() > 10) ){
            errors.rejectValue("latitude","Format");
        }

        if(StringUtils.hasText(longitude) && (Pattern.compile("[^0-9\\-]").matcher(longitude).find() || longitude.replace("-", "").trim().isEmpty() || longitude.length() > 10)){
            errors.rejectValue("longitude","Format");
        }

        if (mode.equals("write")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "obscd", "NotBlank");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "obsnm", "NotBlank");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "type", "NotBlank");

            if (obscd.length() != 8) {
                errors.rejectValue("obscd", "Size");
            }
        }


        //3. 유효성 체크
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "latitude", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "longitude", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "obsknd", "NotBlank");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mngorg", "NotBlank");

        System.out.printf("lat=%s, lon=%s%n", latitude, longitude);
    }
}

