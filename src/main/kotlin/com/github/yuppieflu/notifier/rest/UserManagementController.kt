package com.github.yuppieflu.notifier.rest

import com.github.yuppieflu.notifier.domain.NewUserRequest
import com.github.yuppieflu.notifier.domain.User
import com.github.yuppieflu.notifier.service.UserManagementService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/user")
class UserManagementController(
    private val userManagementService: UserManagementService
) {
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody createUserDto: CreateUserDto): UserResponseDto =
        UserResponseDto(userManagementService.createNewUser(createUserDto.toNewUserRequest()))

    @GetMapping("/{userId}")
    @ResponseBody
    fun getUser(@PathVariable userId: UUID): UserResponseDto =
        UserResponseDto(userManagementService.getUserById(userId))
}

data class CreateUserDto(
    val name: String,
    val email: String,
    val timezone: String
) {
    fun toNewUserRequest() = NewUserRequest(
        name = name.trim(),
        email = email.trim(),
        timezone = timezone.trim()
    )
}

data class UserResponseDto(
    val id: UUID,
    val name: String,
    val email: String,
    val timezone: String
) {
    constructor(user: User) : this(
        id = user.id,
        name = user.name,
        email = user.email,
        timezone = user.timezone
    )
}
