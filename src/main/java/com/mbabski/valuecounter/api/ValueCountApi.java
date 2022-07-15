package com.mbabski.valuecounter.api;

import javax.validation.Valid;
import com.mbabski.valuecounter.error.Error;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/valueCounts")
@OpenAPIDefinition(info = @Info(title = "Value Count", description = "Value Count API", version = "1.0",
        contact = @Contact(name = "Marcel Babski", email = "dindane@o2.pl")))
public interface ValueCountApi {

    @Operation(summary = "Create or update a valueCount record",
            description = "Creates a new valueCount record or updates when provided value already exists.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    content = @Content(schema = @Schema(implementation = ValueCount.class)))
    )
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - valueCount successfully modified.",
            content = @Content(schema = @Schema(implementation = ValueCount.class))),
            @ApiResponse(responseCode = "201", description = "Created - valueCount successfully created.",
                    content = @Content(schema = @Schema(implementation = ValueCount.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - validation error.",
                    content = @Content(schema = @Schema(implementation = Error.class), examples = @ExampleObject(EXAMPLE_400))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(schema = @Schema(implementation = Error.class), examples = @ExampleObject(EXAMPLE_500)))})
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ValueCount> createOrUpdateRecord(@RequestBody @Valid ValueCount valueCountRequest);

    @Operation(summary = "Get valueCount record by value",
            description = "Retrieves valueCount record by value if exists.",
            parameters = {@Parameter(in = ParameterIn.PATH, name = "value", description = "value field of a valueCount record",
                    example = "1.2.3.4")})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK - valueCount successfully retrieved.",
            content = @Content(schema = @Schema(implementation = ValueCount.class))),
            @ApiResponse(responseCode = "404", description = "Not Found.",
                    content = @Content(schema = @Schema(implementation = Error.class), examples = @ExampleObject(EXAMPLE_404))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error.",
                    content = @Content(schema = @Schema(implementation = Error.class), examples = @ExampleObject(EXAMPLE_500)))})
    @GetMapping(value = "/{value}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ValueCount> getValueCountByValue(@PathVariable String value);

    String EXAMPLE_400 = "{\n" +
            "    \"status\": 400,\n" +
            "    \"message\": \"Incorrect datetime format\",\n" +
            "    \"details\": \"firstSeen field datetime format must match 'yyyy-MM-dd HH:mm:ss.SSS' pattern\"\n" +
            "}";

    String EXAMPLE_404 = "{\n" +
            "  \"status\": 404,\n" +
            "  \"message\": \"NOT_FOUND\",\n" +
            "  \"details\": \"Record of value '1.2.3.4' cannot be found\"\n" +
            "}";

    String EXAMPLE_500 = "{\n" +
            "    \"status\": 500,\n" +
            "    \"message\": \"Internal Server Error\",\n" +
            "    \"details\": null\n" +
            "}";
}
