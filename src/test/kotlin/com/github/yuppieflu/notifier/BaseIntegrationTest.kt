package com.github.yuppieflu.notifier

import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.containers.wait.strategy.Wait

open class BaseIntegrationTest {
    companion object {
        init {
            val mongoContainer = MongoDBContainer("mongo:4.4.0")
                .waitingFor(Wait.forLogMessage(".*Waiting for connections.*", 1))
            mongoContainer.start()
            System.setProperty("spring.data.mongodb.uri", mongoContainer.replicaSetUrl)
        }
    }
}
