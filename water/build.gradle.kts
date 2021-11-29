import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot") version "2.6.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.spring") version "1.6.0"
    id("org.springframework.experimental.aot") version "0.11.0-M1"
}

group = "dev.dowell"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://repo.spring.io/milestone") }
    mavenCentral()
}

dependencies {
    val springCloudStreamVersion = "3.2.0-RC1"
    val springCloudFunctionVersion = "3.1.5"
    val jsoupVersion = "1.14.3"

    implementation("org.springframework.cloud:spring-cloud-stream:$springCloudStreamVersion")
    implementation("org.springframework.cloud:spring-cloud-function-kotlin:$springCloudFunctionVersion")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka:$springCloudStreamVersion")
    implementation("org.springframework.kafka:spring-kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream:$springCloudStreamVersion:test-binder")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jsoup:jsoup:$jsoupVersion")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<BootBuildImage> {
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf("BP_NATIVE_IMAGE" to "true")
}

springAot {
    debugVerify.set(true)
}
