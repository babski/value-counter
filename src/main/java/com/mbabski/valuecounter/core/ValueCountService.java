package com.mbabski.valuecounter.core;

import com.mbabski.valuecounter.api.ValueCount;
import com.mbabski.valuecounter.error.ValueCountNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor()
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ValueCountService {

    ValueCountRepository repository;

    public ValueCount createOrUpdateValueCount(ValueCount request) {
        return existsByValue(request.getValue()) ? repository.update(request) : repository.save(request);
    }

    public boolean existsByValue(String value) {
        return repository.existsByValue(value);
    }

    public ValueCount getValueCount(String value) {
        return repository.findByValue(value)
                .orElseThrow(() -> new ValueCountNotFoundException(value));
    }
}
