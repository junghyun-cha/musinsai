plugins {
    val kotlinVersion = "1.9.25"
    val springVersion = "3.5.4"
    val springDependencyManagementVersion = "1.1.7"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    id("org.springframework.boot") version springVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion // QueryDSL 용 Annotation Processor
}

group = "com.choa.musinsai"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allprojects {
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("io.spring.dependency-management")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
        plugin("org.jetbrains.kotlin.plugin.jpa")
        plugin("org.jetbrains.kotlin.kapt")
    }

    // API 모듈에만 Spring Boot 플러그인 적용
    if (project.name == "api") {
        apply(plugin = "org.springframework.boot")
    } else {
        // Core 모듈은 Spring Boot BOM만 적용
        the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
            imports {
                mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.4")
            }
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xjsr305=strict")
        }

        sourceSets.main {
            kotlin.srcDir("${layout.buildDirectory}/generated/source/kapt/main")
        }
    }

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
        all {
            exclude(module = "slf4j-log4j12")
            exclude(module = "log4j-slf4j-impl")
        }
    }

    dependencies {
        implementation(kotlin("stdlib-jdk8"))

        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }
}

project(":api") {
    dependencies {
        implementation(project(":core"))
    }
}

tasks.jar {
    enabled = true
}

tasks.bootJar {
    enabled = false
}

tasks.withType<Test> {
    useJUnitPlatform()
}
