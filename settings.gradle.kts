rootProject.name = "minestom-data"
// DataGenerator
include("DataGenerator")

pluginManagement {
    repositories {
        maven(url = "https://repo.spongepowered.org/repository/maven-public/")
        gradlePluginPortal()
        maven("https://eldonexus.de/repository/maven-public/")
    }
}
