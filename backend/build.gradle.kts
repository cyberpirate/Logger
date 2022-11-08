import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.7.20"
    application
    kotlin("plugin.serialization") version "1.7.20"
    id("com.github.johnrengelman.shadow").version("7.1.2")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val linuxImplementation by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

val winImplementation by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

val macImplementation by configurations.creating {
    extendsFrom(configurations.implementation.get())
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    implementation("me.friwi:jcefmaven:105.3.36")

//    Linux AMD64
    linuxImplementation("me.friwi:jcef-natives-linux-amd64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Linux ARM
//    implementation("me.friwi:jcef-natives-linux-arm:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Linux ARM64
//    implementation("me.friwi:jcef-natives-linux-arm64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Macosx AMD64
    macImplementation("me.friwi:jcef-natives-macosx-amd64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Macosx ARM64
//    implementation("me.friwi:jcef-natives-macosx-arm64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
//    Windows AMD64
    winImplementation("me.friwi:jcef-natives-windows-amd64:jcef-0cdd84c+cef-105.3.36+g88e0038+chromium-105.0.5195.102")
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

var baseJarConfig: (ShadowJar.() -> Unit) = {
    group = "shadow"

    archiveBaseName.set("logger")
    archiveVersion.set("0.1")

//    from {
//        configurations.runtimeClasspath.collect { it.isDirectory() ? it : zipTree(it) }
//    }
    configurations.add(project.configurations.compileClasspath.get())
    from(sourceSets.main.get().output)
    from("./assets") {
        into("assets")
    }

    manifest {
        attributes(
            "Main-Class" to "MainKt"
        )
    }
}

tasks.register<ShadowJar>("onlineJar") {
    baseJarConfig(this)
    archiveClassifier.set("online")
}

tasks.register<ShadowJar>("linuxJar") {
    baseJarConfig(this)
    archiveClassifier.set("linux")
    configurations.add(linuxImplementation)
}

tasks.register<ShadowJar>("winJar") {
    baseJarConfig(this)
    archiveClassifier.set("windows")
    configurations.add(winImplementation)
}

tasks.register<ShadowJar>("macJar") {
    baseJarConfig(this)
    archiveClassifier.set("mac")
    configurations.add(macImplementation)
}

tasks.register("allJars") {
    group = "shadow"
    dependsOn("onlineJar", "linuxJar", "winJar", "macJar")
}