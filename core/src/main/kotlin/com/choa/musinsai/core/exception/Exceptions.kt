package com.choa.musinsai.core.exception

class CrawlerException(
    message: String,
    val platform: String? = null,
    val errorCode: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)

class AIServiceException(
    message: String,
    val provider: String? = null,
    val errorCode: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)

class ValidationException(
    message: String,
    val field: String? = null,
    val value: Any? = null,
    cause: Throwable? = null
) : Exception(message, cause)

class RateLimitException(
    message: String,
    val retryAfter: Long? = null,
    cause: Throwable? = null
) : Exception(message, cause)

class DataProcessingException(
    message: String,
    val dataType: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)
