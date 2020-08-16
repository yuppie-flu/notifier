package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.UserNotFoundException
import com.github.yuppieflu.notifier.db.UserRepository
import com.github.yuppieflu.notifier.domain.NewUserRequest
import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException
import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.TimeZone
import java.util.UUID

@Service
class UserManagementService(
    private val userRepository: UserRepository,
    @Value("\${notifier.delivery-hour:8}") private val deliveryHourLocalTime: Int
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
                utcDeliveryHour = calculateUtcDeliveryHour(request.timezone),
                enabled = true,
                subreddits = emptyList()
            )
        )
        return userRepository.persist(user)
    }

    private fun calculateUtcDeliveryHour(timezone: String): Int =
        runCatching {
            val javaTimeZone = TimeZone.getTimeZone(timezone)
            val utcOffset = Duration.of(
                javaTimeZone.getOffset(System.currentTimeMillis()).toLong(), ChronoUnit.MILLIS
            ).toHours().toInt()
            return deliveryHourLocalTime - utcOffset
        }.fold(
            onFailure = { throw IllegalArgumentException("Incorrect timezone: $timezone") },
            onSuccess = { it }
        )
}
