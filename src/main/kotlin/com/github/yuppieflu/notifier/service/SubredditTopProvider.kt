package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.domain.PostMeta
import com.github.yuppieflu.notifier.domain.SubredditTop
import org.springframework.stereotype.Service
import java.net.URL

interface SubredditTopProvider {
    fun getTopPosts(subreddit: String): SubredditTop
}

@Service
class StaticSubredditTopProvider : SubredditTopProvider {
    override fun getTopPosts(subreddit: String): SubredditTop =
        SubredditTop(
            name = "kotlin",
            topPosts = listOf(
                PostMeta(
                    subreddit = subreddit,
                    title = "Kotlin 1.4.0 stable",
                    score = 165,
                    url = URL("https://www.reddit.com/r/Kotlin/comments/i9oh3p/kotlin_140_stable/")
                )
            )
        )
}
