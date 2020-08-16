package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.BaseIntegrationTest
import com.github.yuppieflu.notifier.util.testUser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class TemporaryUserSubscriptionProcessorIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var subscriptionProcessor: UserSubscriptionProcessorService

    @Test
    fun `test notification`() {
        val user = testUser()

        subscriptionProcessor.fetchDataAndNotify(user)
    }
}
