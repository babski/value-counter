package com.mbabski.valuecounter.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Value;

@Value
public class Error {

    @Schema(type = "integer", example = "400")
    int status;

    @Schema(type = "string", example = "Validation error.")
    String message;

    @Schema(type = "string", example = "totalCount must be less than or equal to 100")
    String details;
}
