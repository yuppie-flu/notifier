package com.github.yuppieflu.notifier.service

import com.github.yuppieflu.notifier.domain.SubredditTop

interface SubredditTopProvider {
    fun getTopPosts(subreddit: String): SubredditTop
}
