package com.github.yuppieflu.notifier.db

import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PgUserRepository : UserRepository {

    override fun persist(user: User): User {
        transaction {
            val existingUserResultRow = joinByUserId(user.id)
            existingUserResultRow?.let {
                updateExistingUser(user)
            } ?: createNewUser(user)
        }
        return user
    }

    private fun createNewUser(user: User) {
        val subscriptionResult = SubscriptionTable.insert {
            it[id] = UUID.randomUUID()
            it[utcDeliveryHour] = user.subscription.utcDeliveryHour
            it[enabled] = user.subscription.enabled
            it[subreddits] = user.subscription.subreddits.joinToString(",")
        }
        UserTable.insert {
            it[id] = user.id
            it[name] = user.name
            it[email] = user.email
            it[timezone] = user.timezone
            it[subscriptionId] = subscriptionResult[SubscriptionTable.id]
        }
    }

    private fun updateExistingUser(user: User) {
        SubscriptionTable.update {
            it[utcDeliveryHour] = user.subscription.utcDeliveryHour
            it[enabled] = user.subscription.enabled
            it[subreddits] = user.subscription.subreddits.joinToString(",")
        }
        UserTable.update {
            it[name] = user.name
            it[email] = user.email
            it[timezone] = user.timezone
        }
    }

    override fun findById(userId: UUID): User? =
        transaction { joinByUserId(userId) }?.toUser()

    override fun findByDeliveryHourAndEnabledSubscription(hour: Byte): List<User> =
        transaction {
            addLogger(StdOutSqlLogger)
            UserTable.join(SubscriptionTable, JoinType.INNER)
                .select {
                    SubscriptionTable.utcDeliveryHour eq hour and
                        (SubscriptionTable.enabled eq true)
                }
                .map { it.toUser() }
        }

    private fun joinByUserId(userId: UUID): ResultRow? =
        UserTable.join(SubscriptionTable, JoinType.INNER)
            .select { UserTable.id eq userId }.firstOrNull()

    private fun ResultRow.toUser(): User =
        User(
            this[UserTable.id].value,
            this[UserTable.name],
            this[UserTable.email],
            this[UserTable.timezone],
            Subscription(
                this[SubscriptionTable.utcDeliveryHour],
                this[SubscriptionTable.enabled],
                this[SubscriptionTable.subreddits].split(",")
            )
        )
}

object UserTable : UUIDTable(name = "users") {
    val name = varchar("name", 50)
    val email = varchar("email", 50).uniqueIndex()
    val timezone = varchar("timezone", 50)
    val subscriptionId = reference("subscription_id", SubscriptionTable.id)
}

object SubscriptionTable : UUIDTable(name = "subscriptions") {
    val utcDeliveryHour = byte("utc_delivery_hour").index()
    val enabled = bool("enabled")
    val subreddits = text("subreddits")
}
