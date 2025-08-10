package com.choa.musinsai.core.config

import io.micrometer.observation.ObservationRegistry
import org.springframework.ai.bedrock.converse.BedrockProxyChatModel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeAsyncClient
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient
import java.time.Duration

@Configuration
@EnableConfigurationProperties(BedrockModelProperties::class)
class AiConfig {

    @Bean
    fun chatClient(anthropicChatModel: BedrockProxyChatModel): ChatClient {
        return ChatClient.create(anthropicChatModel)
    }

    @Bean
    fun bedrockRuntimeClient(awsCredentialsProvider: AwsCredentialsProvider): BedrockRuntimeClient =
        BedrockRuntimeClient
            .builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(awsCredentialsProvider)
            .overrideConfiguration(
                ClientOverrideConfiguration
                    .builder()
                    .apiCallTimeout(Duration.ofMinutes(5))
                    .apiCallAttemptTimeout(Duration.ofMinutes(5))
                    .build()
            )
            .build()

    @Bean
    fun bedrockRuntimeAsyncClient(awsCredentialsProvider: AwsCredentialsProvider): BedrockRuntimeAsyncClient =
        BedrockRuntimeAsyncClient
            .builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(awsCredentialsProvider)
            .overrideConfiguration(
                ClientOverrideConfiguration
                    .builder()
                    .apiCallTimeout(Duration.ofMinutes(5))
                    .apiCallAttemptTimeout(Duration.ofMinutes(5))
                    .build()
            )
            .build()

    @Bean
    fun observationRegistry(): ObservationRegistry = ObservationRegistry.NOOP
}
