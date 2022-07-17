package com.mbabski.valuecounter.rest

import com.mbabski.valuecounter.api.ValueCount
import com.mbabski.valuecounter.core.ValueCountService
import com.mbabski.valuecounter.error.DefaultExceptionHandler
import com.mbabski.valuecounter.error.GlobalExceptionHandler
import com.mbabski.valuecounter.error.ValueCountNotFoundException
import io.restassured.RestAssured
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import static com.mbabski.valuecounter.ValueCountFixtures.ANY_VALUE
import static com.mbabski.valuecounter.ValueCountFixtures.CREATE_EXCEEDED_COUNT
import static com.mbabski.valuecounter.ValueCountFixtures.CREATE_FUTURE_DATE_TIME
import static com.mbabski.valuecounter.ValueCountFixtures.CREATE_FUTURE_DATE_TIME_AND_EXCEEDED_COUNT
import static com.mbabski.valuecounter.ValueCountFixtures.CREATE_INCORRECT_DATE_TIME_FORMAT
import static com.mbabski.valuecounter.ValueCountFixtures.CREATE_NEGATIVE_COUNT
import static com.mbabski.valuecounter.ValueCountFixtures.CREATE_VALUE_COUNT
import static com.mbabski.valuecounter.error.GlobalExceptionHandler.DATETIME_FORMAT_DETAILS
import static com.mbabski.valuecounter.error.GlobalExceptionHandler.DATETIME_FORMAT_MESSAGE
import static com.mbabski.valuecounter.error.GlobalExceptionHandler.VALIDATION_MESSAGE

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ComponentScan(excludeFilters = [@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration)])
@Import([ValueCountController, DefaultExceptionHandler, GlobalExceptionHandler])
class ValueCountControllerIT extends Specification {

    @SpringBean
    private ValueCountService valueCountService = Mock()

    @Value('${local.server.port}')
    private Integer serverPort

    def 'Attempt to create a valueCount with a unique value returns 201 HTTP response code status'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_VALUE_COUNT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.CREATED.value()
    }

    def 'Attempt to create a valueCount with already persisted value returns 200 HTTP response code status'() {
        given:
            valueCountService.existsByValue(ANY_VALUE) >> true
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_VALUE_COUNT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.OK.value()
    }

    def 'Attempt to create a valueCount with a count below 0 returns 400 HTTP response code status'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_NEGATIVE_COUNT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            with(response.body().jsonPath()) {
                status == 400
                message == VALIDATION_MESSAGE
                details == 'totalCount must be greater than or equal to 0'
            }
    }

    def 'Attempt to create a valueCount with a count above 100 returns 400 HTTP response code status'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_EXCEEDED_COUNT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            with(response.body().jsonPath()) {
                status == 400
                message == VALIDATION_MESSAGE
                details == 'totalCount must be less than or equal to 100'
            }
    }


    def 'Attempt to create a valueCount with a future firstSeen datetime returns 400 HTTP response code status'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_FUTURE_DATE_TIME)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            with(response.body().jsonPath()) {
                status == 400
                message == VALIDATION_MESSAGE
                details == 'firstSeen must be a past date'
            }
    }

    def 'Attempt to create a valueCount with a future firstSeen datetime and count above 100 returns 400 HTTP response code status with respective details'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_FUTURE_DATE_TIME_AND_EXCEEDED_COUNT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.BAD_REQUEST.value()
            with(response.body().jsonPath()) {
                status == 400
                message == VALIDATION_MESSAGE
                details.contains('firstSeen must be a past date')
                details.contains('totalCount must be less than or equal to 100')
            }
    }

    def 'Attempt to create a valueCount with an incorrect firstSeen datetime format returns 400 HTTP response code status'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_INCORRECT_DATE_TIME_FORMAT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode == HttpStatus.BAD_REQUEST.value
            with(response.body().jsonPath()) {
                status == 400
                message == DATETIME_FORMAT_MESSAGE
                details == DATETIME_FORMAT_DETAILS
            }
    }

    def 'Unknown error during a valueCount creation returns 500 HTTP response code status'() {
        given:
            valueCountService.createOrUpdateValueCount(_ as ValueCount) >> { throw new Exception() }
            final request = RestAssured.given().port(serverPort)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON.toString())
                    .body(CREATE_VALUE_COUNT)

        when:
            final response = request.post('/api/valueCounts')

        then:
            response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()
            with(response.body().jsonPath()) {
                status == 500
                message == HttpStatus.INTERNAL_SERVER_ERROR.name()
                details == null
            }
    }

    def 'Fetching existing valueCount by value returns 200 HTTP response code status'() {
        given:
            final request = RestAssured.given().port(serverPort)
                    .accept(MediaType.APPLICATION_JSON.toString())

        when:
            final response = request.get('/api/valueCounts/' + ANY_VALUE)

        then:
            response.statusCode() == HttpStatus.OK.value()
    }

    def 'Trying to fetch non-existing valueCount by value returns 404 HTTP response code status'() {
        given:
            valueCountService.getValueCount(ANY_VALUE) >> { throw new ValueCountNotFoundException(ANY_VALUE) }
            final request = RestAssured.given().port(serverPort)
                    .accept(MediaType.APPLICATION_JSON.toString())

        when:
            final response = request.get('/api/valueCounts/' + ANY_VALUE)

        then:
            response.statusCode() == HttpStatus.NOT_FOUND.value()
            with(response.body().jsonPath()) {
                status == 404
                message == HttpStatus.NOT_FOUND.name()
                details == "Record of value '" + ANY_VALUE + "' cannot be found"
            }
    }

    def 'Unknown error during fetching a valueCount returns 500 HTTP response code status'() {
        given:
            valueCountService.getValueCount(ANY_VALUE) >> { throw new Exception() }
            final request = RestAssured.given().port(serverPort)
                    .accept(MediaType.APPLICATION_JSON.toString())

        when:
            final response = request.get('/api/valueCounts/' + ANY_VALUE)

        then:
            response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR.value()
            response.body().jsonPath().get("status") == 500
            response.body().jsonPath().get("message") == HttpStatus.INTERNAL_SERVER_ERROR.name()
            response.body().jsonPath().get("details") == null
    }

}
