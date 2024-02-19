package org.sundo.list.services;

import org.springframework.http.HttpStatus;
import org.sundo.commons.Utils;
import org.sundo.commons.exceptions.AlertBackException;

public class ObservatoryDataNotFoundException extends AlertBackException {
    public ObservatoryDataNotFoundException () {
        super(Utils.getMessage("NotFound.bordData", "errors"), HttpStatus.NOT_FOUND);
    }
}
