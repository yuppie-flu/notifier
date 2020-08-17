package com.github.yuppieflu.notifier.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuppieflu.notifier.InvalidTimezoneException
import com.github.yuppieflu.notifier.UserNotFoundException
import com.github.yuppieflu.notifier.service.UserManagementService
import com.github.yuppieflu.notifier.util.testUserInputDto
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

@WebMvcTest(UserManagementController::class)
class UserManagementControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockBean
    private lateinit var userManagementServiceMock: UserManagementService

    @Test
    fun `should return not found for getting unknown user`() {
        // given
        val userId = UUID.randomUUID()
        given(userManagementServiceMock.getUserById(userId))
            .willThrow(UserNotFoundException::class.java)

        // expect
        mockMvc.perform(get("/api/v1/user/$userId"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should return bad request for unknown timezone`() {
        // given
        val request = testUserInputDto()
        given(userManagementServiceMock.createNewUser(request.toNewUserRequest()))
            .willThrow(InvalidTimezoneException::class.java)

        // expect
        mockMvc.perform(
            post("/api/v1/user")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest)
    }
}
