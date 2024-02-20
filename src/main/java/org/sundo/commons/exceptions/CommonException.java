package org.sundo.commons.exceptions;

import org.springframework.http.HttpStatus;

public class CommonException extends RuntimeException{
    private HttpStatus status;

    public CommonException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public CommonException(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}