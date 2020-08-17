package com.github.yuppieflu.notifier.service.impl

import com.github.yuppieflu.notifier.domain.PostMeta
import com.github.yuppieflu.notifier.domain.SubredditTop
import com.github.yuppieflu.notifier.service.SubredditTopProvider
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.lang.IllegalStateException
import java.net.URL

@Service
class RestTemplateSubredditTopProvider(
    private val restTemplate: RestTemplate,
    @Value("\${notifier.posts-limit:3}") private val postsLimit: Int,
    @Value("\${notifier.reddit.url:}") private val redditUrl: String
) : SubredditTopProvider {

    override fun getTopPosts(subreddit: String): SubredditTop {
        val httpEntity = HttpEntity<String>(headers)

        // TODO: add error retrying and rate limiting
        // Using https://github.com/resilience4j/resilience4j for example
        val response = restTemplate.exchange(
            subredditTopQueryUrl(subreddit),
            HttpMethod.GET,
            httpEntity,
            RedditPostListingResponse::class.java
        )
        // TODO: check response status code

        return response.body?.data?.children?.map {
            PostMeta(
                subreddit = subreddit,
                title = it.data.title,
                score = it.data.score,
                url = fullPostUrl(it.data.permalink)
            )
        }?.let {
            SubredditTop(
                name = subreddit,
                topPosts = it
            )
        } ?: throw IllegalStateException("Something went wrong when querying Reddit!")
    }

    private fun fullPostUrl(postfix: String): URL =
        URL("$redditUrl$postfix")

    private fun subredditTopQueryUrl(subreddit: String) =
        "$redditUrl/r/$subreddit/top/.json?limit=$postsLimit&t=day"

    companion object {
        private val headers = HttpHeaders().also {
            it.set(HttpHeaders.USER_AGENT, "kotlin-reddit-notifier")
        }
    }
}

private data class RedditPostListingResponse(
    val data: Data
)

private data class Data(
    val children: List<Child>
)

private data class Child(
    val kind: String,
    val data: ChildData
)

private data class ChildData(
    val id: String,
    val title: String,
    val score: Int,
    val permalink: String
)
