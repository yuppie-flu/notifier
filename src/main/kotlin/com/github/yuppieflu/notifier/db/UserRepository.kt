package com.github.yuppieflu.notifier.db

import com.github.yuppieflu.notifier.domain.User
import java.util.UUID

interface UserRepository {
    fun persist(user: User): User
    fun findById(userId: UUID): User?
    fun findByDeliveryHourAndEnabledSubscription(hour: Int): List<User>
}
