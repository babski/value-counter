package com.mbabski.valuecounter.error;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValueCountNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Record of value '%s' cannot be found";

    public ValueCountNotFoundException(String value) {
        super(String.format(MESSAGE, value));
    }
}
