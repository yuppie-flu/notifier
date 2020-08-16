package com.github.yuppieflu.notifier.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.web.client.RestTemplate

@Configuration
@EnableScheduling
class NotifierSpringConfig {

    @Bean
    fun restTemplate() = RestTemplate()
}
