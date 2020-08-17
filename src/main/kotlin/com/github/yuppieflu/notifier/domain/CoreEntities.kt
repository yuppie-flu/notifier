package com.github.yuppieflu.notifier.domain

import java.net.URL
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val timezone: String,
    val subscription: Subscription
)

data class Subscription(
    val utcDeliveryHour: Int,
    val enabled: Boolean,
    val subreddits: List<String>
)

data class NotificationPackage(
    val username: String,
    val email: String,
    val subreddits: List<SubredditTop>
)

data class SubredditTop(
    val name: String,
    val topPosts: List<PostMeta>
) {
    val topUrl: URL
        get() = URL("https://www.reddit.com/r/$name/top")
}

data class PostMeta(
    val subreddit: String,
    val title: String,
    val score: Int,
    val url: URL
)
