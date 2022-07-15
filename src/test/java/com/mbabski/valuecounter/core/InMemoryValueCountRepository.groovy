package com.mbabski.valuecounter.core

import com.mbabski.valuecounter.api.ValueCount
import com.mbabski.valuecounter.core.ValueCountRepository

class InMemoryValueCountRepository implements ValueCountRepository {
    private final Map<String, ValueCount> valueCounts = new HashMap<>()

    @Override
    Optional<ValueCount> findByValue(String value) {
        return Optional.ofNullable(valueCounts.get(value))
    }

    @Override
    boolean existsByValue(String value) {
        return valueCounts.get(value) != null
    }

    @Override
    ValueCount save(ValueCount valueCount) {
        valueCounts.put(valueCount.getValue(), valueCount)
        return valueCount
    }

    @Override
    ValueCount update(ValueCount valueCount) {
        valueCounts.computeIfPresent(valueCount.getValue(), (k, v) ->
                new ValueCount(valueCount.getType(), k, v.getFirstSeen(), valueCount.getTotalCount()))
        return valueCounts.get(valueCount.getValue())
    }
}