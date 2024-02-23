package org.sundo.wamis.services;

import org.sundo.commons.Utils;
import org.springframework.http.HttpStatus;
import org.sundo.commons.exceptions.AlertBackException;

public class ObservationNotFoundException extends AlertBackException {
    public ObservationNotFoundException() {
        super(Utils.getMessage("NotFound.bordData", "errors"), HttpStatus.NOT_FOUND);
    }
}
