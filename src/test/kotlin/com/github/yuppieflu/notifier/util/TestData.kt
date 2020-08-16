package com.github.yuppieflu.notifier.util

import com.github.yuppieflu.notifier.domain.NewUserRequest
import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import com.github.yuppieflu.notifier.rest.CreateUserDto
import java.util.UUID

// Domain entities

private const val DEFAULT_NAME = "Username"
private const val DEFAULT_TIMEZONE = "Europe/Berlin"
private fun defaultEmail(id: UUID) =
    "${id.toString().replace('-', '.')}@example.com"

fun testUser(
    id: UUID = UUID.randomUUID(),
    name: String = DEFAULT_NAME,
    email: String = defaultEmail(id),
    timeZone: String = DEFAULT_TIMEZONE,
    utcDeliveryHour: Int = 6,
    subscriptionEnabled: Boolean = true,
    subreddits: List<String> = listOf("kotlin")
) = User(
    id = id,
    name = name,
    email = email,
    timezone = timeZone,
    subscription = Subscription(
        utcDeliveryHour = utcDeliveryHour,
        enabled = subscriptionEnabled,
        subreddits = subreddits
    )
)

// REST related DTOs

fun testNewUserRequest(
    name: String = DEFAULT_NAME,
    email: String = defaultEmail(UUID.randomUUID()),
    timeZone: String = DEFAULT_TIMEZONE
) =
    NewUserRequest(
        name = name,
        email = email,
        timezone = timeZone
    )

fun testCreateUserDto(
    name: String = DEFAULT_NAME,
    email: String = defaultEmail(UUID.randomUUID()),
    timeZone: String = DEFAULT_TIMEZONE
) = CreateUserDto(
    name = name,
    email = email,
    timezone = timeZone
)
