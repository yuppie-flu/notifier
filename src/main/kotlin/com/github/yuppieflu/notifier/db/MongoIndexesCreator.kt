package com.github.yuppieflu.notifier.db

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver
import org.springframework.data.mongodb.core.mapping.MongoMappingContext
import org.springframework.stereotype.Service

@Service
class MongoIndexesCreator(
    private val mongoTemplate: MongoTemplate,
    private val mongoMappingContext: MongoMappingContext
) {
    @EventListener(ApplicationReadyEvent::class)
    fun initIndicesAfterStartup() {
        val indexOps = mongoTemplate.indexOps(UserEntity::class.java)
        val resolver = MongoPersistentEntityIndexResolver(mongoMappingContext)
        resolver.resolveIndexFor(UserEntity::class.java).forEach { indexOps.ensureIndex(it) }
    }
}
