package com.mbabski.valuecounter.api;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
public class ValueCount {

    @Schema(type = "string", example = "ipv4")
    String type;

    @Schema(type = "string", example = "1.2.3.4")
    String value;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @Past
    @Schema(type = "string", example = "2020-07-10 15:00:00.000")
    LocalDateTime firstSeen;

    @Min(value = 0)
    @Max(value = 100)
    @Schema(type = "integer", example = "8")
    int totalCount;

}
