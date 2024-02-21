package org.sundo.wamis.services;

import org.springframework.http.HttpStatus;
import org.sundo.commons.exceptions.AlertBackException;

public class ObservatoryNotFoundException extends AlertBackException {
    public ObservatoryNotFoundException(){
        super("해당 관측소를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
    }
}
