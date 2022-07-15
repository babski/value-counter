package com.mbabski.valuecounter.rest;

import javax.validation.Valid;
import com.mbabski.valuecounter.api.ValueCount;
import com.mbabski.valuecounter.api.ValueCountApi;
import com.mbabski.valuecounter.core.ValueCountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
class ValueCountController implements ValueCountApi {

    ValueCountService valueCountService;

    public ResponseEntity<ValueCount> createOrUpdateRecord(@RequestBody @Valid ValueCount valueCountRequest) {
        boolean exists = valueCountService.existsByValue(valueCountRequest.getValue());
        ValueCount valueCount = valueCountService.createOrUpdateValueCount(valueCountRequest);
        log.info("Value count of value {}", valueCountRequest.getValue() + (exists ? " updated" : " created"));
        return exists ? ResponseEntity.ok(valueCount) : new ResponseEntity<>(valueCount, HttpStatus.CREATED);
    }

    public ResponseEntity<ValueCount> getValueCountByValue(@PathVariable String value) {
        return ResponseEntity.ok(valueCountService.getValueCount(value));
    }
}
