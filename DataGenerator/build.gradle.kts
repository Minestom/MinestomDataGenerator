plugins {
    java
    application
    alias(libs.plugins.vanilla.gradle)
}

group = "net.minestom"
version = rootProject.version

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
    version(libs.versions.minecraft.get())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}