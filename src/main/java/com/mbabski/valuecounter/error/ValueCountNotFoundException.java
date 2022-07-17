package com.mbabski.valuecounter.error;

public class ValueCountNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Record of value '%s' cannot be found";

    public ValueCountNotFoundException(String value) {
        super(String.format(MESSAGE, value));
    }
}
