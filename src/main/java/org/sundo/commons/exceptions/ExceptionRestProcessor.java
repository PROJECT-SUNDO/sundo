package org.sundo.commons.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.sundo.commons.rests.JSONData;

/**
 * Rest 컨트롤러 예외처리
 */
public interface ExceptionRestProcessor {

    @ExceptionHandler(Exception.class)
    default ResponseEntity<JSONData<Object>> errorHandler(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;  // 기본 500

        if(e instanceof CommonException) {
            CommonException commonException = (CommonException) e;
            status = commonException.getStatus();
        }

        JSONData<Object> data = new JSONData<>();
        data.setSuccess(false);
        data.setStatus(status);
        data.setMessage(e.getMessage());

        e.printStackTrace();

        return ResponseEntity.status(status).body(data);

    }
}