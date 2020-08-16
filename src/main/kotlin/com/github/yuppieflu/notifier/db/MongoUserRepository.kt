package com.github.yuppieflu.notifier.db

import com.github.yuppieflu.notifier.domain.Subscription
import com.github.yuppieflu.notifier.domain.User
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class MongoUserRepositoryImpl(
    private val springDataRepo: MongoSpringDataRepository
) : UserRepository {
    override fun persist(user: User): User =
        springDataRepo.save(UserEntity(user)).toDomain()

    override fun findById(userId: UUID): User? =
        springDataRepo.findById(userId).orElse(null)?.toDomain()

    override fun findByDeliveryHour(hour: Int): List<User> =
        springDataRepo.findByUtcDeliveryHour(hour).map { it.toDomain() }
}

@Repository
interface MongoSpringDataRepository : MongoRepository<UserEntity, UUID> {
    @Query(value = "{ 'subscription.utcDeliveryHour' : ?0 }")
    fun findByUtcDeliveryHour(hour: Int): List<UserEntity>
}

@Document(collection = "user")
data class UserEntity(
    @Id
    val id: UUID,
    val name: String,
    @Indexed(unique = true)
    val email: String,
    val timezone: String,
    val subscription: SubscriptionEntity
) {
    constructor(user: User) :
        this(
            id = user.id,
            name = user.name,
            email = user.email,
            timezone = user.timezone,
            subscription = SubscriptionEntity(
                utcDeliveryHour = user.subscription.utcDeliveryHour,
                enabled = user.subscription.enabled,
                subreddits = user.subscription.subreddits
            )
        )

    fun toDomain() =
        User(
            id = id,
            name = name,
            email = email,
            timezone = timezone,
            subscription = Subscription(
                utcDeliveryHour = subscription.utcDeliveryHour,
                enabled = subscription.enabled,
                subreddits = subscription.subreddits
            )
        )
}

data class SubscriptionEntity(
    @Indexed
    val utcDeliveryHour: Int,
    val enabled: Boolean,
    val subreddits: List<String>
)
