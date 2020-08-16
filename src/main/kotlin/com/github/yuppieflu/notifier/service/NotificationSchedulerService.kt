package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.db.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class NotificationScheduler(
    private val userRepository: UserRepository,
    private val subscriptionProcessor: UserSubscriptionProcessorService
) {

    @Scheduled(cron = "0 0 * * * *")
    fun scheduleNotification() {
        val utcHour = LocalDateTime.now(ZoneOffset.UTC).hour
        userRepository.findByDeliveryHour(utcHour).forEach {
            subscriptionProcessor.fetchDataAndNotify(it)
        }
    }
}

fun main(args: Array<String>) {
    println(LocalDateTime.now(ZoneOffset.UTC).hour)
}
