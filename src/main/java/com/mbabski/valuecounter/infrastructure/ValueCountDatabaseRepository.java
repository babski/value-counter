package com.mbabski.valuecounter.infrastructure;

import java.util.Optional;
import com.mbabski.valuecounter.api.ValueCount;
import com.mbabski.valuecounter.core.ValueCountRepository;
import com.mbabski.valuecounter.error.ValueCountNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Repository
class ValueCountDatabaseRepository implements ValueCountRepository {

    ValueCountCRUDRepository repository;

    @Override
    public Optional<ValueCount> findByValue(String value) {
        return repository.findByValue(value)
                .map(ValueCountEntity::toDomain);
    }

    @Override
    public boolean existsByValue(String value) {
        return repository.existsByValue(value);
    }

    @Override
    public ValueCount save(ValueCount valueCount) {
        return repository.save(new ValueCountEntity(valueCount))
                .toDomain();
    }

    @Override
    public ValueCount update(ValueCount valueCount) {
        return repository.findByValue(valueCount.getValue())
                .map(entity -> entity.modifyAndGet(valueCount))
                .map(entity -> repository.save(entity).toDomain())
                .orElseThrow(() -> new ValueCountNotFoundException(valueCount.getValue()));
    }
}
