package com.mbabski.valuecounter.error;

import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class GlobalExceptionHandler {

    static final String VALIDATION_MESSAGE = "Validation error";
    static final String DATETIME_FORMAT_MESSAGE = "Incorrect datetime format";
    static final String DATETIME_FORMAT_DETAILS = "firstSeen field datetime format must match 'yyyy-MM-dd HH:mm:ss.SSS' pattern";

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    Error handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        String details = result.getFieldErrors().stream()
                .map(this::createADetailedMessage)
                .collect(Collectors.joining(", "));
        log.warn(details);
        return new Error(BAD_REQUEST.value(), VALIDATION_MESSAGE, details);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(DateTimeParseException.class)
    Error handleDateTimeParseException(DateTimeParseException exception) {
        log.warn("{}. {}", DATETIME_FORMAT_MESSAGE, exception.getMessage());
        return new Error(BAD_REQUEST.value(), DATETIME_FORMAT_MESSAGE, DATETIME_FORMAT_DETAILS);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ValueCountNotFoundException.class)
    Error handleValueCountNotFoundException(ValueCountNotFoundException exception) {
        log.warn(exception.getMessage());
        return new Error(NOT_FOUND.value(), NOT_FOUND.name(), exception.getMessage());
    }

    private String createADetailedMessage(FieldError fieldError) {
        return fieldError.getField() + " " + fieldError.getDefaultMessage();
    }


}
