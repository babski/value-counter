package com.mbabski.valuecounter.infrastructure;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ValueCountCRUDRepository extends CrudRepository<ValueCountEntity, Long> {

    Optional<ValueCountEntity> findByValue(String value);

    boolean existsByValue(String value);
}
