package com.choa.musinsai.core.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.bedrock")
class BedrockModelProperties {
    lateinit var model: Map<String, String>
    var role: RoleProperties? = null

    class RoleProperties {
        lateinit var arn: String
    }
}
