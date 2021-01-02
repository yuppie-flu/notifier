package com.github.yuppieflu.notifier.config

import org.jetbrains.exposed.sql.Database
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct
import javax.sql.DataSource

@Configuration
class PgConfig(private val dataSource: DataSource) {

    @PostConstruct
    fun initExposed() {
        Database.connect(dataSource)
    }
}
