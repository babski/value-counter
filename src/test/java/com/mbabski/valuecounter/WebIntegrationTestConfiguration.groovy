package com.mbabski.valuecounter

import com.mbabski.valuecounter.error.DefaultExceptionHandler
import com.mbabski.valuecounter.error.GlobalExceptionHandler
import com.mbabski.valuecounter.core.ValueCountService
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import spock.mock.DetachedMockFactory

@TestConfiguration
@EnableAutoConfiguration
@Import([GlobalExceptionHandler, DefaultExceptionHandler])
class WebIntegrationTestConfiguration {

    @Bean
    ValueCountService valueCountService(){
        return new DetachedMockFactory().Mock(ValueCountService)
    }
}
