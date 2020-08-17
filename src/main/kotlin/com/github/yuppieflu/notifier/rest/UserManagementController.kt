package com.github.yuppieflu.notifier.rest

import com.github.yuppieflu.notifier.ValidationException
import com.github.yuppieflu.notifier.domain.NewUserRequest
import com.github.yuppieflu.notifier.domain.SubscriptionUpdateRequest
import com.github.yuppieflu.notifier.domain.UpdateUserRequest
import com.github.yuppieflu.notifier.domain.User
import com.github.yuppieflu.notifier.service.UserManagementService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/user")
class UserManagementController(
    private val userManagementService: UserManagementService,
    @Value("\${notifier.maxSubreddits:5}") private val maxSubreddits: Int
) {
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userInputDto: UserInputDto): UserResponseDto =
        UserResponseDto(userManagementService.createNewUser(userInputDto.toNewUserRequest()))

    @GetMapping("/{userId}")
    @ResponseBody
    fun getUser(@PathVariable userId: UUID): UserResponseDto =
        UserResponseDto(userManagementService.getUserById(userId))

    @PutMapping("/{userId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateUser(
        @PathVariable userId: UUID,
        @RequestBody userInputDto: UserInputDto
    ) = userManagementService.updateUser(userInputDto.toUpdateUserRequest(userId))

    @PutMapping("/{userId}/subscription")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateSubscription(
        @PathVariable userId: UUID,
        @RequestBody subscriptionDto: SubscriptionDto
    ) {
        if (subscriptionDto.subreddits.size > maxSubreddits) {
            throw ValidationException("Too many subreddits! Only max $maxSubreddits is allowed.")
        }
        userManagementService.updateSubscription(
            subscriptionDto.toSubscriptionUpdateRequest(userId)
        )
    }
}

data class UserInputDto(
    val name: String,
    val email: String,
    val timezone: String
) {
    fun toNewUserRequest() = NewUserRequest(
        name = name.trim(),
        email = email.trim(),
        timezone = timezone.trim()
    )

    fun toUpdateUserRequest(userId: UUID) = UpdateUserRequest(
        userId = userId,
        name = name.trim(),
        email = email.trim(),
        timezone = timezone.trim()
    )
}

data class UserResponseDto(
    val id: UUID,
    val name: String,
    val email: String,
    val timezone: String,
    val subscription: SubscriptionDto
) {
    constructor(user: User) : this(
        id = user.id,
        name = user.name,
        email = user.email,
        timezone = user.timezone,
        subscription = SubscriptionDto(
            enabled = user.subscription.enabled,
            subreddits = user.subscription.subreddits
        )
    )
}

data class SubscriptionDto(
    val enabled: Boolean,
    val subreddits: List<String>
) {
    fun toSubscriptionUpdateRequest(userId: UUID) =
        SubscriptionUpdateRequest(
            userId = userId,
            enabled = enabled,
            subreddits = subreddits.map { it.trim() }
        )
}
