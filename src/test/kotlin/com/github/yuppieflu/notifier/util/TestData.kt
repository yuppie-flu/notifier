package com.github.yuppieflu.notifier.util

import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import com.github.yuppieflu.notifier.rest.CreateUserDto
import java.util.UUID

// Domain entities

fun testUser(
    id: UUID = UUID.randomUUID(),
    name: String = "Username",
    email: String = "$name@example.com",
    timeZone: String = "Europe/Berlin",
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

// REST DTOs

fun testCreateUserDto(
    name: String = "name",
    email: String = "$name@example.com",
    timeZone: String = "Europe/Berlin"
) = CreateUserDto(
    name = name,
    email = email,
    timezone = timeZone
)
