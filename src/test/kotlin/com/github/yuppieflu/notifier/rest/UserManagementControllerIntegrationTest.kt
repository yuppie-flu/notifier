package com.github.yuppieflu.notifier.rest

import com.github.yuppieflu.notifier.BaseIntegrationTest
import com.github.yuppieflu.notifier.util.testCreateUserDto
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
    fun `should create and get a new user`() {
        // given
        val createUserDto = testCreateUserDto()

        // when
        val createUserResponse = testRestTemplate.postForEntity(
            "/api/v1/user", createUserDto, UserResponseDto::class.java
        )
        // then
        with(createUserResponse) {
            expectThat(statusCode).isEqualTo(HttpStatus.CREATED)
            expectThat(body).isNotNull()
            expectThat(body?.email).isEqualTo(createUserDto.email)
        }

        // when
        val getNewUserResponse = testRestTemplate.getForEntity(
            "/api/v1/user/${createUserResponse.body?.id}", UserResponseDto::class.java
        )

        // then
        with(getNewUserResponse) {
            expectThat(statusCode).isEqualTo(HttpStatus.OK)
            expectThat(body).isNotNull()
            expectThat(body?.email).isEqualTo(createUserDto.email)
        }
    }
}
