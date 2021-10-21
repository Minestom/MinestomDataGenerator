group = "net.minestom"
plugins {
    id("maven-publish")
}

allprojects {
    version = "1.0"
}

val mcVersion = project.properties["mcVersion"].toString()
val outputDirectory = (findProperty("output") ?: rootDir.resolve("MinestomData").absolutePath) as String

tasks {
    register("generateData") {
        logger.warn("Mojang requires all source-code and mappings used to be governed by the Minecraft EULA.")
        logger.warn("Please read the Minecraft EULA located at https://account.mojang.com/documents/minecraft_eula.")
        logger.warn("In order to agree to the EULA you must create a file called eula.txt with the text 'eula=true'.")
        val eulaTxt = File("${rootProject.projectDir}/eula.txt")
        logger.warn("The file must be located at '${eulaTxt.absolutePath}'.")
        if ((eulaTxt.exists() && eulaTxt.readText(Charsets.UTF_8)
                .equals("eula=true", true)) || project.properties["eula"].toString().toBoolean()
        ) {
            logger.warn("")
            logger.warn("The EULA has been accepted and signed.")
            logger.warn("")
        } else {
            throw GradleException("Data generation has been halted as the EULA has not been signed.")
        }
        logger.warn("It is unclear if the data from the data generator also adhere to the Minecraft EULA.")
        logger.warn("Please consult your own legal team!")
        logger.warn("All data is given independently without warranty, guarantee or liability of any kind.")
        logger.warn("The data may or may not be the intellectual property of Mojang Studios.")
        logger.warn("")

        // Simplified by Sponge's VanillaGradle
        dependsOn(
            project(":DataGenerator").tasks.getByName<JavaExec>("run") {
                args = arrayListOf(outputDirectory)
            }
        )

    }
    register<Jar>("dataJar") {
        dependsOn("generateData")

        archiveBaseName.set("minestom-data")
        archiveVersion.set(mcVersion)
        destinationDirectory.set(layout.buildDirectory.dir("dist"))
        from(outputDirectory)
    }
}

publishing {
    publications {
        create<MavenPublication>("MinestomData") {
            groupId = "net.minestom"
            artifactId = "minestom-data"
            version = mcVersion

            artifact(tasks.getByName("dataJar"))
        }
    }
}