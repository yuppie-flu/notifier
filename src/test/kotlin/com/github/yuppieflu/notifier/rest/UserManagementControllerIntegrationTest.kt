package com.github.yuppieflu.notifier.rest

import com.github.yuppieflu.notifier.BaseIntegrationTest
import com.github.yuppieflu.notifier.util.testSubscriptionDto
import com.github.yuppieflu.notifier.util.testUserInputDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserManagementControllerIntegrationTest : BaseIntegrationTest() {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun `should create new user and get created user`() {
        // given
        val createUserDto = testUserInputDto()

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
    }

    @Test
    fun `should update user data and subscription`() {
        // given
        val createUserDto = testUserInputDto()
        val createUserResponse = testRestTemplate.postForEntity(
            API_PREFIX, createUserDto, UserResponseDto::class.java
        )
        expectThat(createUserResponse.statusCode).isEqualTo(HttpStatus.CREATED)
        val userId = createUserResponse.body!!.id

        // when updating user data
        val updateUserInputDto = testUserInputDto(email = "updated@example.com")
        val updateUserResponse = testRestTemplate.exchange(
            "$API_PREFIX/$userId",
            HttpMethod.PUT,
            HttpEntity(updateUserInputDto),
            String::class.java
        )

        // then
        expectThat(updateUserResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        // when updating subscription
        val subscriptionDto = testSubscriptionDto()
        val subscriptionUpdateResponse = testRestTemplate.exchange(
            "$API_PREFIX/$userId/subscription",
            HttpMethod.PUT,
            HttpEntity(subscriptionDto),
            String::class.java
        )

        // then
        expectThat(subscriptionUpdateResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        // when
        val getUpdatedUserResponse = testRestTemplate.getForEntity(
            "$API_PREFIX/$userId", UserResponseDto::class.java
        )

        // then
        with(getUpdatedUserResponse) {
            expectThat(statusCode).isEqualTo(HttpStatus.OK)
            expectThat(body).isNotNull()
            expectThat(body?.email).isEqualTo(updateUserInputDto.email)
            expectThat(body?.subscription?.subreddits).isEqualTo(subscriptionDto.subreddits)
        }
    }

    companion object {
        private const val API_PREFIX = "/api/v1/user"
    }
}
