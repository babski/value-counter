package com.mbabski.valuecounter

import com.mbabski.valuecounter.api.ValueCount

import java.time.LocalDate
import java.time.LocalDateTime

class ValueCountFixtures {
    static final String ANY_TYPE = 'ipv4'
    static final String ANY_VALUE = '1.2.3.4'
    static final LocalDateTime YESTERDAY_NOON = LocalDate.now().minusDays(1).atTime(12, 0)
    static final int ANY_COUNT = 6

    static ValueCount get() {
        new ValueCount(ANY_TYPE, ANY_VALUE, YESTERDAY_NOON, ANY_COUNT)
    }

    static ValueCount getAnotherHavingTheSameValueField() {
        def newDateTime = LocalDateTime.of(2020, 1, 1, 1, 1)
        new ValueCount('new-type', ANY_VALUE, newDateTime, 99)
    }

    static ValueCount getAnotherHavingDifferentFields() {
        def newDateTime = LocalDateTime.of(2020, 2, 1, 1, 1)
        new ValueCount('new-type2', 'any-value', newDateTime, 19)
    }

    static final def CREATE_VALUE_COUNT = """
{
    "type": "ipv4",
    "value": "1.2.3.4",
    "firstSeen": "2021-01-02 00:00:00.969",
    "totalCount": 35
}
"""

    static final def CREATE_NEGATIVE_COUNT = """
{
    "type": "ipv4",
    "value": "1.2.3.4",
    "firstSeen": "2021-01-02 00:00:00.969",
    "totalCount": -15
}
"""

    static final def CREATE_EXCEEDED_COUNT = """
{
    "type": "ipv4",
    "value": "1.2.3.4",
    "firstSeen": "2021-01-02 00:00:00.969",
    "totalCount": 115
}
"""

    static final def CREATE_FUTURE_DATE_TIME_AND_EXCEEDED_COUNT = """
{
    "type": "ipv4",
    "value": "1.2.3.4",
    "firstSeen": "2032-01-02 00:00:00.969",
    "totalCount": 115
}
"""

    static final def CREATE_FUTURE_DATE_TIME = """
{
    "type": "ipv4",
    "value": "1.2.3.4",
    "firstSeen": "2032-01-02 00:00:00.969",
    "totalCount": 11
}
"""

    static final def CREATE_INCORRECT_DATE_TIME_FORMAT = """
{
    "type": "ipv4",
    "value": "1.2.3.4",
    "firstSeen": "2012-01-02 00:00:00",
    "totalCount": 11
}
"""
}
