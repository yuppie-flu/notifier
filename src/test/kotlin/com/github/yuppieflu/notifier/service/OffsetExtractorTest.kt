package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.InvalidTimezoneException
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure

class OffsetExtractorTest {

    private val underTest = OffsetExtractor()

    @ParameterizedTest
    @CsvSource(
        value = [
            "Europe/Moscow, 3",
            "Asia/Tokyo, 9",
            "US/Hawaii, -10"
        ]
    )
    fun `should extract offset from UCT in hours`(timezone: String, expectedOffset: Int) {
        // expect
        expectThat(underTest.getUtcOffsetInHours(timezone)).isEqualTo(expectedOffset)
    }

    @ParameterizedTest
    @ValueSource(strings = ["invalid zone", "America/Hawaii"])
    fun `should throw exception for unknown timezone`(invalidTimezone: String) {
        // expect
        expectCatching { underTest.getUtcOffsetInHours(invalidTimezone) }
            .isFailure()
            .isA<InvalidTimezoneException>()
    }
}
