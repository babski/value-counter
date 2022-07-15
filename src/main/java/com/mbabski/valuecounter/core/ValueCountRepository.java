package com.mbabski.valuecounter.core;

import java.util.Optional;
import com.mbabski.valuecounter.api.ValueCount;

public interface ValueCountRepository {

    Optional<ValueCount> findByValue(String value);

    boolean existsByValue(String value);

    ValueCount save(ValueCount valueCount);

    ValueCount update(ValueCount valueCount);
}
