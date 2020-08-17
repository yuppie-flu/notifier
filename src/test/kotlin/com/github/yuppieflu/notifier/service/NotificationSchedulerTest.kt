package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.db.UserRepository
import com.github.yuppieflu.notifier.util.testUser
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.Mockito.mock
import java.time.LocalDateTime
import java.time.ZoneOffset

class NotificationSchedulerTest {

    private val userRepositoryMock = mock(UserRepository::class.java)

    private val subscriptionProcessorMock = mock(SubscriptionProcessor::class.java)

    private val underTest = NotificationScheduler(userRepositoryMock, subscriptionProcessorMock)

    @Test
    fun `should continue processing if exception was thrown for one user`() {
        // given
        val user1 = testUser()
        val user2 = testUser()
        val utcHour = LocalDateTime.now(ZoneOffset.UTC).hour

        given(userRepositoryMock.findByDeliveryHourAndEnabledSubscription(utcHour))
            .willReturn(listOf(user1, user2))
        given(subscriptionProcessorMock.fetchDataAndNotify(user1)).willThrow(RuntimeException("test"))

        // when
        underTest.scheduleNotification()

        // then
        verify(subscriptionProcessorMock).fetchDataAndNotify(user2)
    }
}
