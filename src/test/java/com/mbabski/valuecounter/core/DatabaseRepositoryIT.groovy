package com.mbabski.valuecounter.core

import com.mbabski.valuecounter.infrastructure.DatabaseIntegrationTestConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.DirtiesContext

import java.sql.Timestamp

import static com.mbabski.valuecounter.ValueCountFixtures.anotherHavingDifferentFields
import static com.mbabski.valuecounter.ValueCountFixtures.anotherHavingTheSameValueField
import static com.mbabski.valuecounter.ValueCountFixtures.get

class DatabaseRepositoryIT extends DatabaseIntegrationTestConfig {

    @Autowired
    ValueCountRepository repository

    @Autowired
    JdbcTemplate jdbcTemplate

    @DirtiesContext
    def 'ValueCount is correctly persisted in repository'() {
        given:
            def valueCount = get()

        when:
            repository.save(valueCount)

        then:
            def results = jdbcTemplate.queryForList("SELECT * FROM value_count")
            results.size() == 1
            results[0]['type'] == valueCount.getType()
            results[0]['value_field'] == valueCount.getValue()
            results[0]['first_Seen'] == Timestamp.valueOf(valueCount.getFirstSeen())
            results[0]['total_Count'] == valueCount.getTotalCount()
    }

    @DirtiesContext
    def 'ValueCount of another value field is correctly persisted in repository'() {
        given:
            def valueCount1 = get()
            repository.save(valueCount1)
            def valueCount2 = getAnotherHavingDifferentFields()

        when:
            repository.save(valueCount2)

        then:
            def results = jdbcTemplate.queryForList("SELECT * FROM value_count")
            results.size() == 2
    }

    @DirtiesContext
    def 'ValueCount of the same value field as persisted is correctly updated in repository'() {
        given:
            def valueCount1 = get()
            def valueCount2 = getAnotherHavingTheSameValueField()
            repository.save(valueCount1)

        when:
            repository.update(valueCount2)

        then:
            def results = jdbcTemplate.queryForList("SELECT * FROM value_count")
            results.size() == 1
            results[0]['type'] == valueCount2.getType()
            results[0]['value_field'] == valueCount1.getValue()
            results[0]['first_Seen'] == Timestamp.valueOf(valueCount1.getFirstSeen())
            results[0]['total_Count'] == valueCount2.getTotalCount()
    }

    @DirtiesContext
    def 'ValueCount existence is correctly retrieved from repository'() {
        given:
            def valueCount = get()
            repository.save(valueCount)

        when:
            def exists = repository.existsByValue(valueCount.getValue())

        then:
            exists
    }

    @DirtiesContext
    def 'ValueCount is correctly retrieved from repository'() {
        given:
            def valueCount = get()
            repository.save(valueCount)

        when:
            def valueCountOptional = repository.findByValue(valueCount.getValue())

        then:
            valueCountOptional.isPresent()
            valueCountOptional.get() == valueCount
    }

}
