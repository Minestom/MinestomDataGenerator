plugins {
    java
    application
    alias(libs.plugins.loom)
}

group = "net.minestom"
version = rootProject.version

dependencies {
    minecraft("com.mojang:minecraft:${libs.versions.minecraft.get()}")

    implementation(libs.gson)
    //implementation(libs.bundles.logging) Server comes with logging
}

application {
    mainClass.set("net.minestom.datagen.DataGen")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
    // Don't use modules here, as it's prone to breakage every update
}

loom {
    serverOnlyMinecraftJar()
    accessWidenerPath = file("src/main/resources/minestom.classtweaker")
}
