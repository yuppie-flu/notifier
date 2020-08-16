package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.domain.NotificationPackage
import com.github.yuppieflu.notifier.domain.User
import org.springframework.stereotype.Service

@Service
class UserSubscriptionProcessorService(
    private val subredditTopProvider: SubredditTopProvider,
    private val notifier: Notifier
) {
    fun fetchDataAndNotify(user: User): Unit =
        user.subscription.subreddits.map {
            subredditTopProvider.getTopPosts(it)
        }.let {
            NotificationPackage(
                username = user.name,
                email = user.email,
                subreddits = it
            )
        }.let {
            notifier.notify(it)
        }
}
