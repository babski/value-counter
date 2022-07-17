package com.mbabski.valuecounter.core

import com.mbabski.valuecounter.infrastructure.DatabaseIntegrationTestConfig
import com.mbabski.valuecounter.infrastructure.ValueCountCRUDRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

import static com.mbabski.valuecounter.ValueCountFixtures.anotherHavingDifferentFields
import static com.mbabski.valuecounter.ValueCountFixtures.anotherHavingTheSameValueField
import static com.mbabski.valuecounter.ValueCountFixtures.get

class DatabaseRepositoryIT extends DatabaseIntegrationTestConfig {

    @Autowired
    ValueCountRepository repository

    @Autowired
    ValueCountCRUDRepository valueCountCRUDRepository

    @Autowired
    TestEntityManager testEntityManager

    def 'ValueCount is correctly persisted in repository'() {
        given:
            def valueCount = get()

        when:
            repository.save(valueCount)

        and:
            flushData()

        then:
            def results = valueCountCRUDRepository.findAll()
            results.size() == 1
            with(results.first()) {
                type == valueCount.type
                value == valueCount.value
                totalCount == valueCount.totalCount
                firstSeen == valueCount.firstSeen
            }
    }

    def 'ValueCount of another value field is correctly persisted in repository'() {
        given:
            def valueCount1 = get()
            repository.save(valueCount1)
            def valueCount2 = getAnotherHavingDifferentFields()

        when:
            repository.save(valueCount2)

        and:
            flushData()

        then:
            valueCountCRUDRepository.findAll().size() == 2
    }

    def 'ValueCount of the same value field as persisted is correctly updated in repository'() {
        given:
            def valueCount1 = get()
            def valueCount2 = getAnotherHavingTheSameValueField()
            repository.save(valueCount1)

        when:
            repository.update(valueCount2)

        and:
            flushData()

        then:
            def results = valueCountCRUDRepository.findAll()
            results.size == 1
        and:
            with(results.first()) {
                type == valueCount2.getType()
                value == valueCount1.getValue()
                firstSeen == valueCount1.getFirstSeen()
                totalCount == valueCount2.getTotalCount()
            }
    }

    def 'ValueCount existence is correctly retrieved from repository'() {
        given:
            def valueCount = get()
            repository.save(valueCount)

        and:
            flushData()

        expect:
            repository.existsByValue(valueCount.getValue())
    }

    def 'ValueCount is correctly retrieved from repository'() {
        given:
            def valueCount = get()
            repository.save(valueCount)

        and:
            flushData()

        when:
            def valueCountOptional = repository.findByValue(valueCount.getValue())

        then:
            valueCountOptional.present
            valueCountOptional.get() == valueCount
    }

    private void flushData() {
        testEntityManager.flush()
        testEntityManager.clear()
    }

}
