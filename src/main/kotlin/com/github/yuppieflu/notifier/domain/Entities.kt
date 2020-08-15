package com.github.yuppieflu.notifier.domain

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val timezone: String,
    val subscription: Subscription
)

data class Subscription(
    val enabled: Boolean,
    val subreddits: List<String>
)

data class NewUserRequest(
    val name: String,
    val email: String,
    val timezone: String
)
