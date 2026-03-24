//enableFeaturePreview("VERSION_CATALOGS")

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
