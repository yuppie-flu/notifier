package com.github.yuppieflu.notifier

import com.github.yuppieflu.notifier.rest.CreateUserDto
import com.github.yuppieflu.notifier.rest.UserResponseDto
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.Wait

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserManagementControllerIntegrationTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    companion object {
        @BeforeAll
        @JvmStatic
        fun startMongo() {
            val mongoContainer = MongoDBContainer("mongo:4.4.0")
                .waitingFor(
                    Wait.forLogMessage(
                        ".*Waiting for connections.*",
                        1
                    )
                )
            mongoContainer.start()
            System.setProperty("spring.data.mongodb.uri", mongoContainer.replicaSetUrl)
        }
    }

    @Test
    fun `should create and get a new user`() {
        // given
        val createUserDto = CreateUserDto(
            name = "test",
            email = "test@example.org",
            timezone = "UTC"
        )

        // when
        val createUserResponse = testRestTemplate.postForEntity(
            "/api/v1/user", createUserDto, UserResponseDto::class.java
        )
        val getNewUserResponse = testRestTemplate.getForEntity(
            "/api/v1/user/${createUserResponse.body?.id}", UserResponseDto::class.java
        )

        // then
        println(getNewUserResponse.body)
    }
}
