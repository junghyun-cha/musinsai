package com.choa.musinsai.core.utils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.jvm.java

inline fun <reified T> logger(): Logger {
    return LoggerFactory.getLogger(T::class.java)
}
