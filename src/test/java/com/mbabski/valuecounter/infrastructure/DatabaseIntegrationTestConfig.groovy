package com.mbabski.valuecounter.infrastructure

import com.mbabski.valuecounter.WebIntegrationTestConfiguration
import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

@SpringBootConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@EnableAutoConfiguration
@Import(WebIntegrationTestConfiguration)
class DatabaseIntegrationTestConfig extends Specification {
}
