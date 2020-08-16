package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.InternalServerException
import com.github.yuppieflu.notifier.InvalidTimezoneException
import com.github.yuppieflu.notifier.UserNotFoundException
import com.github.yuppieflu.notifier.db.UserRepository
import com.github.yuppieflu.notifier.domain.NewUserRequest
import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.time.DateTimeException
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.zone.ZoneRulesException
import java.util.UUID

@Service
class UserManagementService(
    private val userRepository: UserRepository,
    private val offsetExtractor: OffsetExtractor,
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
        deliveryHourLocalTime - offsetExtractor.getUtcOffsetInHours(timezone)
}

@Component
class OffsetExtractor {

    private val log = LoggerFactory.getLogger(OffsetExtractor::class.java)

    fun getUtcOffsetInHours(timezone: String): Int =
        runCatching {
            Duration.of(
                ZoneId.of(timezone).rules.getOffset(Instant.now()).totalSeconds.toLong(),
                ChronoUnit.SECONDS
            ).toHours().toInt()
        }.fold(
            onFailure = {
                when (it) {
                    is ZoneRulesException, is DateTimeException -> {
                        log.warn("timezone parsing failed: ${it.message}")
                        throw InvalidTimezoneException(timezone)
                    }
                    else -> throw InternalServerException(it)
                }
            },
            onSuccess = { it }
        )
}
