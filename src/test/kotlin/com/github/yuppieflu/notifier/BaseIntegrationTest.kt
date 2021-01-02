package com.github.yuppieflu.notifier

import com.github.yuppieflu.notifier.config.PgConfig
import com.github.yuppieflu.notifier.db.UserTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Component
import org.testcontainers.containers.PostgreSQLContainer
import javax.annotation.PostConstruct

open class BaseIntegrationTest {
    companion object {
        init {
            val postgresContainer = PostgreSQLContainer<Nothing>("postgres:13.1")
            postgresContainer.start()
            println("Postgres URL: ${postgresContainer.jdbcUrl}")
            System.setProperty("spring.datasource.url", postgresContainer.jdbcUrl)
            System.setProperty("spring.datasource.username", postgresContainer.username)
            System.setProperty("spring.datasource.password", postgresContainer.password)
        }
    }
}

@Component
class SchemaCreator(private val pgConfig: PgConfig) {

    @PostConstruct
    @DependsOn("PgConfig")
    fun createTables() {
        transaction {
            SchemaUtils.create(UserTable)
        }
    }
}
