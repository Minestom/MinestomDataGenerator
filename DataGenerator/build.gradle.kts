import org.spongepowered.gradle.vanilla.MinecraftExtension

plugins {
    java
    application
}

apply {
    plugin("org.spongepowered.gradle.vanilla")
}

group = "net.minestom"
version = rootProject.version

dependencies {
    implementation(libs.gson)
    implementation(libs.bundles.logging)
}

application {
    mainClass.set("net.minestom.datagen.DataGen")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

extensions.configure<MinecraftExtension> {
    version(libs.versions.minecraft.get())
    platform(org.spongepowered.gradle.vanilla.repository.MinecraftPlatform.SERVER)
}
