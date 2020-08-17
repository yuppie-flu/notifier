package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.db.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class NotificationScheduler(
    private val userRepository: UserRepository,
    private val subscriptionProcessor: SubscriptionProcessor
) {
    private val log = LoggerFactory.getLogger(NotificationScheduler::class.java)

    @Scheduled(cron = "0 0 * * * *")
    fun scheduleNotification() {
        val utcHour = LocalDateTime.now(ZoneOffset.UTC).hour
        userRepository.findByDeliveryHourAndEnabledSubscription(utcHour).forEach { user ->
            runCatching {
                subscriptionProcessor.fetchDataAndNotify(user)
            }.fold(
                onFailure = {
                    th ->
                    log.error("Error while handling subscription for userId [${user.id}]", th)
                },
                onSuccess = {}
            )
        }
    }
}
