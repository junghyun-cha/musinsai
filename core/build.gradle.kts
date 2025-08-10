plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

// Core 모듈은 라이브러리이므로 jar만 생성
tasks.jar {
    enabled = true
}
dependencies {
    implementation("org.springframework.ai:spring-ai-starter-model-bedrock:1.0.0")
    // AWS SSO 의존성 추가
    implementation("software.amazon.awssdk:sso:2.31.30")
    implementation("software.amazon.awssdk:ssooidc:2.31.30")
// https://mvnrepository.com/artifact/software.amazon.awssdk/sts
    implementation("software.amazon.awssdk:sts:2.32.19")
// HTTP Client and Spring Web
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

// Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

// Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

// Netty DNS resolver for macOS (fixes DNS warning)
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.100.Final:osx-aarch_64")

// Testing
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}
