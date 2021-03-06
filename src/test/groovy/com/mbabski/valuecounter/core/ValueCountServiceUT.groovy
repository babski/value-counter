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

        expect:
            valueCountService.existsByValue(ANY_VALUE)
    }

    def 'Any record does not exist in the empty repository'() {
        expect:
            !valueCountService.existsByValue(ANY_VALUE)
    }

    def 'An attempt to fetch any valueCount from empty repository throws the exception'() {
        when:
            valueCountService.getValueCount(ANY_VALUE)

        then:
            ValueCountNotFoundException exception = thrown()
            exception.message == "Record of value '1.2.3.4' cannot be found"
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
            int givenTotalCount = 13
            String givenNewType = 'new-type'

        when:
            valueCountService.createOrUpdateValueCount(new ValueCount(givenNewType, ANY_VALUE, today, givenTotalCount))

        then:
            with(valueCountService.getValueCount(ANY_VALUE)) {
                type == givenNewType
                totalCount == givenTotalCount
                value == ANY_VALUE
                firstSeen == YESTERDAY_NOON
            }
    }

}
