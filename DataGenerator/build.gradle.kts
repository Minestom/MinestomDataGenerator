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
    // SLF4J is the base logger for most libraries.
    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("com.google.code.gson:gson:2.8.8")
}

application {
    mainClass.set("net.minestom.datagen.DataGen")
}
java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

minecraft {
    version(project.rootProject.properties["mcVersion"].toString())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}