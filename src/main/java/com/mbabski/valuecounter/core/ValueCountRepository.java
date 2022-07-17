package com.mbabski.valuecounter.core;

import java.util.Optional;
import com.mbabski.valuecounter.api.ValueCount;
import org.springframework.transaction.annotation.Transactional;

public interface ValueCountRepository {

    Optional<ValueCount> findByValue(String value);

    boolean existsByValue(String value);

    ValueCount save(ValueCount valueCount);

    @Transactional
    ValueCount update(ValueCount valueCount);
}
