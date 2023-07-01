group = "net.minestom"

plugins {
    java
    application
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.gson)
    implementation(libs.bundles.logging)
}

application {
    mainClass.set("net.minestom.datagen.DataGen")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

minecraft {
    version(project.rootProject.properties["mcVersion"].toString())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}