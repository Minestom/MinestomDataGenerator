import org.spongepowered.gradle.vanilla.MinecraftExtension

plugins {
    java
    application
    alias(libs.plugins.vanilla.gradle) apply true
}

group = "net.minestom"
version = rootProject.version

dependencies {
    implementation(libs.gson)
    //implementation(libs.bundles.logging) Server comes with logging
}

application {
    mainClass.set("net.minestom.datagen.DataGen")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

extensions.configure<MinecraftExtension> {
    version(libs.versions.minecraft.get())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}
