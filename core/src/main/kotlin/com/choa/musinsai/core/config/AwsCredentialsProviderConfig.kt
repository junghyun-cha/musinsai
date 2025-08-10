package com.choa.musinsai.core.config

import com.choa.musinsai.core.utils.logger
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sts.StsClient
import software.amazon.awssdk.services.sts.auth.StsAssumeRoleCredentialsProvider
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest

@Configuration
class AwsCredentialsProviderConfig {
    private val logger = logger<AwsCredentialsProviderConfig>()

    @Value("\${aws.credentials.profile:default}")
    private lateinit var awsProfile: String

    @Value("\${aws.bedrock.role.arn:default}")
    private lateinit var bedrockRoleArn: String

    private val region = Region.AP_NORTHEAST_2

    // 로컬 환경에서도 Assume Role을 사용하여 Bedrock 접근
    @Bean
    @Profile("local")
    fun awsCredentialsProvider(): AwsCredentialsProvider {
        logger.info("로컬 환경에서 Assume Role 사용: $bedrockRoleArn")

        return try {
            // 먼저 SSO 프로파일로 기본 자격 증명 획득
            val baseCredentialsProvider = ProfileCredentialsProvider.builder()
                .profileName(awsProfile)
                .build()

            baseCredentialsProvider.resolveCredentials() // 유효성 체크
            logger.info("SSO 자격 증명 성공적으로 로드됨")

// STS 클라이언트 생성 (SSO 자격 증명 사용)
            val stsClient = StsClient.builder()
                .region(region)
                .credentialsProvider(baseCredentialsProvider)
                .build()

// Assume Role 수행
            val assumeRoleProvider = StsAssumeRoleCredentialsProvider.builder()
                .refreshRequest(
                    AssumeRoleRequest.builder()
                        .roleArn(bedrockRoleArn)
                        .roleSessionName("bedrock-local-session-" + System.currentTimeMillis())
                        .durationSeconds(3600)
                        .build()
                )
                .stsClient(stsClient)
                .build()

            logger.info("Assume Role 성공: $bedrockRoleArn")
            assumeRoleProvider
        } catch (e: Exception) {
            logger.error("로컬 환경에서 Assume Role 실패: ${e.message}", e)
            throw kotlin.RuntimeException("로컬 환경에서 AWS Assume Role 실패", e)
        }
    }

    // 개발, 운영 환경에선 Assume Role을 사용하여 AWS 서비스에 접근함.
    @Bean
    @ConditionalOnMissingBean(AwsCredentialsProvider::class)
    @ConditionalOnWebApplication
    fun webAwsCredentialsProvider(): AwsCredentialsProvider {
        logger.info("Initializing webAwsCredentialsProvider")

        return StsAssumeRoleCredentialsProvider.builder()
            .refreshRequest(
                AssumeRoleRequest.builder()
                    .roleArn(bedrockRoleArn)
                    .roleSessionName("bedrock-session-" + System.currentTimeMillis())
                    .durationSeconds(3600)
                    .build()
            )
            .stsClient(
                StsClient.builder()
                    .region(region)
                    .build()
            )
            .build()
    }
}
