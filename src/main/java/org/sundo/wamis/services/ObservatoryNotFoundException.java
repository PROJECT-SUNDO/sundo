package org.sundo.wamis.services;

import org.springframework.http.HttpStatus;
import org.sundo.commons.Utils;
import org.sundo.commons.exceptions.AlertBackException;

public class ObservatoryNotFoundException extends AlertBackException {
    public ObservatoryNotFoundException(){
        super(Utils.getMessage("NotFound.Observatory", "errors"), HttpStatus.NOT_FOUND);
    }
}
