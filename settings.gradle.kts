//enableFeaturePreview("VERSION_CATALOGS")

pluginManagement {
    repositories {
        maven(url = "https://maven.fabricmc.net/")
        gradlePluginPortal()
    }
}

rootProject.name = "minestom-data"
// DataGenerator
include("DataGenerator")
