pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.fabricmc.net/")
    }
}

rootProject.name = "minestom-data"
// DataGenerator
include("DataGenerator")
