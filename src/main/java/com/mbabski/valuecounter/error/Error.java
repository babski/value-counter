package com.mbabski.valuecounter.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class Error {

    @Schema(type = "integer", example = "400")
    int status;

    @Schema(type = "string", example = "Validation error.")
    String message;

    @Schema(type = "string", example = "totalCount must be less than or equal to 100")
    String details;
}
