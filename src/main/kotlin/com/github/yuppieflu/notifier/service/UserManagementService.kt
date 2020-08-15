package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.UserNotFoundException
import com.github.yuppieflu.notifier.db.UserRepository
import com.github.yuppieflu.notifier.domain.NewUserRequest
import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserManagementService(
    private val userRepository: UserRepository
) {

    fun getUserById(userId: UUID): User =
        userRepository.findById(userId) ?: throw UserNotFoundException(userId)

    fun createNewUser(request: NewUserRequest): User {
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            name = request.name,
            email = request.email,
            timezone = request.timezone,
            subscription = Subscription(
                enabled = true,
                subreddits = emptyList()
            )
        )
        return userRepository.persist(user)
    }
}
