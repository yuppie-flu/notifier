package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.domain.NotificationPackage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface Notifier {
    fun notify(aPackage: NotificationPackage)
}

@Service
class ConsoleNotifier : Notifier {
    private val log = LoggerFactory.getLogger(ConsoleNotifier::class.java)

    override fun notify(aPackage: NotificationPackage) {
        log.info("Sending notification:")
        log.info("To: ${aPackage.email}")
        log.info("Username: ${aPackage.username}")
        log.info("Content:")
        aPackage.subreddits.forEach {
            log.info("Subreddit: [${it.name}] ${it.topUrl}")
            it.topPosts.forEach { post ->
                log.info("Post: ${post.title} [${post.score}]")
                log.info("URL: ${post.url}")
            }
        }
    }
}
