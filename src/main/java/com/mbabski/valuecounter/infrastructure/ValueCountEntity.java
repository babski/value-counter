package com.mbabski.valuecounter.infrastructure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import com.mbabski.valuecounter.api.ValueCount;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "value_count")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class ValueCountEntity {

    @GeneratedValue
    @Id
    Long id;

    String type;

    @Column(name = "value_field", unique = true)
    String value;

    LocalDateTime firstSeen;

    int totalCount;

    ValueCountEntity(ValueCount valueCount) {
        this.type = valueCount.getType();
        this.value = valueCount.getValue();
        this.firstSeen = valueCount.getFirstSeen();
        this.totalCount = valueCount.getTotalCount();
    }

    ValueCountEntity modifyAndGet(ValueCount valueCount) {
        this.type = valueCount.getType();
        this.totalCount = valueCount.getTotalCount();
        return this;
    }

    public ValueCount toDomain() {
        return new ValueCount(type, value, firstSeen, totalCount);
    }
}
