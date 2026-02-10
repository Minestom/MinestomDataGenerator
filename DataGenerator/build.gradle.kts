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
}

loom {
    serverOnlyMinecraftJar()
    accessWidenerPath = file("src/main/resources/minestom.classtweaker")
}
