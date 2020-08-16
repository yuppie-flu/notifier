package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.BaseIntegrationTest
import com.github.yuppieflu.notifier.util.testUser
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TemporaryUserSubscriptionProcessorIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var subscriptionProcessor: UserSubscriptionProcessorService

    @Test
    fun `test notification`() {
        val user = testUser(
            subreddits = listOf("berlin", "climbing", "kotlin")
        )

        subscriptionProcessor.fetchDataAndNotify(user)
    }
}
