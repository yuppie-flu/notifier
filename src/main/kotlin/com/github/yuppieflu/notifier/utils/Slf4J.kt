package com.github.yuppieflu.notifier.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

object Slf4J {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Logger =
        thisRef.let {
            if (it::class.isCompanion) {
                it.javaClass.declaringClass
            } else {
                it.javaClass
            }
        }.let {
            LoggerFactory.getLogger(it)
        }
}
