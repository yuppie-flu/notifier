package com.github.yuppieflu.notifier.rest

import com.github.yuppieflu.notifier.BaseIntegrationTest
import com.github.yuppieflu.notifier.util.testCreateUserDto
import com.github.yuppieflu.notifier.util.testSubscriptionInputDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserManagementControllerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should create new user, get this user and update subscription`() {
        // given
        val createUserDto = testCreateUserDto()

        // when
        val createUserResponse = testRestTemplate.postForEntity(
            API_PREFIX, createUserDto, UserResponseDto::class.java
        )
        // then
        with(createUserResponse) {
            expectThat(statusCode).isEqualTo(HttpStatus.CREATED)
            expectThat(body).isNotNull()
            expectThat(body?.email).isEqualTo(createUserDto.email)
        }

        val userId = createUserResponse.body!!.id

        // when
        val getNewUserResponse = testRestTemplate.getForEntity(
            "$API_PREFIX/$userId", UserResponseDto::class.java
        )

        // then
        with(getNewUserResponse) {
            expectThat(statusCode).isEqualTo(HttpStatus.OK)
            expectThat(body).isNotNull()
            expectThat(body?.email).isEqualTo(createUserDto.email)
        }

        // when
        val subscriptionInputDto = testSubscriptionInputDto()
        val subscriptionUpdateResponse = testRestTemplate.postForEntity(
            "$API_PREFIX/$userId/subscription", subscriptionInputDto, UserResponseDto::class.java
        )
        with(subscriptionUpdateResponse) {
            expectThat(statusCode).isEqualTo(HttpStatus.OK)
            expectThat(body).isNotNull()
            expectThat(body?.email).isEqualTo(createUserDto.email)
            expectThat(body?.subscription?.subreddits).isEqualTo(subscriptionInputDto.subreddits)
        }
    }

    companion object {
        private const val API_PREFIX = "/api/v1/user"
    }
}
