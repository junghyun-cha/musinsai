package com.choa.musinsai.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class CoreApplication

fun main(args: Array<String>) {
    runApplication<CoreApplication>(*args)
}
