package com.github.yuppieflu.notifier.domain

import java.util.UUID

data class NewUserRequest(
    val name: String,
    val email: String,
    val timezone: String
)

data class SubscriptionUpdateRequest(
    val userId: UUID,
    val enabled: Boolean?,
    val subreddits: List<String>?
)
