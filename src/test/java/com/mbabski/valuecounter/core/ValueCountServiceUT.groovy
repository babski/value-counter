package com.mbabski.valuecounter.core

import com.mbabski.valuecounter.api.ValueCount
import com.mbabski.valuecounter.error.ValueCountNotFoundException
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalDate

import static com.mbabski.valuecounter.ValueCountFixtures.ANY_COUNT
import static com.mbabski.valuecounter.ValueCountFixtures.ANY_TYPE
import static com.mbabski.valuecounter.ValueCountFixtures.ANY_VALUE
import static com.mbabski.valuecounter.ValueCountFixtures.YESTERDAY_NOON

class ValueCountServiceUT extends Specification {

    def repository = new InMemoryValueCountRepository()

    @Subject
    ValueCountService valueCountService = new ValueCountService(repository)

    def 'Saved ValueCount record exists in repository'() {
        given:
            repository.save(new ValueCount(ANY_TYPE, ANY_VALUE, YESTERDAY_NOON, ANY_COUNT))

        when:
            def exists = valueCountService.existsByValue(ANY_VALUE)

        then:
            exists
    }

    def 'Any record does not exist in the empty repository'() {
        when:
            def exists = valueCountService.existsByValue(ANY_VALUE)

        then:
            !exists
    }

    def 'An attempt to fetch any valueCount from empty repository throws the exception'() {
        when:
            valueCountService.getValueCount(ANY_VALUE)

        then:
            ValueCountNotFoundException exception = thrown()
            exception.getMessage() == "Record of value '1.2.3.4' cannot be found"
    }

    def 'An attempt to fetch persisted valueCount by its value ends up in success'() {
        given:
            def valueCount = new ValueCount(ANY_TYPE, ANY_VALUE, YESTERDAY_NOON, ANY_COUNT)
            repository.save(valueCount)

        when:
            def fetchedValueCount = valueCountService.getValueCount(ANY_VALUE)

        then:
            fetchedValueCount == valueCount
    }

    def 'ValueCount is correctly saved in repository'() {
        given:
            def valueCount = new ValueCount(ANY_TYPE, ANY_VALUE, YESTERDAY_NOON, ANY_COUNT)

        when:
            valueCountService.createOrUpdateValueCount(valueCount)

        then:
            valueCountService.getValueCount(ANY_VALUE) == valueCount
    }

    def 'An attempt to save valueCount with a value existing in repository overrides persisted value leaving firstSeen field unchanged'() {
        given:
            def today = LocalDate.now().atStartOfDay()
            def valueCount = new ValueCount(ANY_TYPE, ANY_VALUE, YESTERDAY_NOON, ANY_COUNT)
            repository.save(valueCount)

        when:
            valueCountService.createOrUpdateValueCount(new ValueCount('new-type', ANY_VALUE, today, 13))

        then:
            def fetchedValueCount = valueCountService.getValueCount(ANY_VALUE)
            fetchedValueCount.getType() == 'new-type'
            fetchedValueCount.getValue() == ANY_VALUE
            fetchedValueCount.getFirstSeen() == YESTERDAY_NOON
            fetchedValueCount.getTotalCount() == 13
    }

}
