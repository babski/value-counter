package com.mbabski.valuecounter.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
class DefaultExceptionHandler {

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    Error handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new Error(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.name(), null);
    }
}
