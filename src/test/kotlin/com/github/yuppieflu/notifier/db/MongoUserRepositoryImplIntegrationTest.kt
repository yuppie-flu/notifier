package com.github.yuppieflu.notifier.db

import com.github.yuppieflu.notifier.BaseIntegrationTest
import com.github.yuppieflu.notifier.util.testUser
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.containsExactlyInAnyOrder

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MongoUserRepositoryImplIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var mongoSpringDataRepository: MongoSpringDataRepository

    @Autowired
    private lateinit var underTest: MongoUserRepositoryImpl

    @BeforeEach
    fun cleanupDB() {
        mongoSpringDataRepository.deleteAll()
    }

    @Test
    fun `should find users by delivery hour and with enabled subscription`() {
        // given
        val expectedDeliveryHour = 6
        val user1 = testUser(utcDeliveryHour = expectedDeliveryHour)
        val user2 = testUser(utcDeliveryHour = 8)
        val user3 = testUser(utcDeliveryHour = expectedDeliveryHour)
        val user4 = testUser(utcDeliveryHour = expectedDeliveryHour, subscriptionEnabled = false)
        listOf(user1, user2, user3, user4).forEach { underTest.persist(it) }

        // when
        val result = underTest.findByDeliveryHourAndEnabledSubscription(expectedDeliveryHour)

        // then
        expectThat(result).containsExactlyInAnyOrder(user1, user3)
    }
}
