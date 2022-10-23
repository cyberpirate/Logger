import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
    kotlin("plugin.serialization") version "1.7.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("me.friwi:jcefmaven:105.3.36")

//    Linux AMD64
    implementation("me.friwi:jcef-natives-linux-amd64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Linux ARM
//    implementation("me.friwi:jcef-natives-linux-arm:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Linux ARM64
//    implementation("me.friwi:jcef-natives-linux-arm64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Macosx AMD64
//    implementation("me.friwi:jcef-natives-macosx-amd64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Macosx ARM64
//    implementation("me.friwi:jcef-natives-macosx-arm64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Windows AMD64
//    implementation("me.friwi:jcef-natives-windows-amd64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Windows ARM64
//    implementation("me.friwi:jcef-natives-windows-arm64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Windows i386
//    implementation("me.friwi:jcef-natives-windows-i386:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}