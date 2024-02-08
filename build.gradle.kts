plugins {
    `java-library`
    alias(libs.plugins.vanilla.gradle) apply false
    `maven-publish`
    signing
    alias(libs.plugins.nexuspublish)
    alias(libs.plugins.publisdata)
    id("net.kyori.indra") version "3.1.3"
    id("net.kyori.indra.publishing") version "3.1.3"
    id("net.kyori.indra.publishing.sonatype") version "3.1.3"
}

group = "net.onelitefeather.microtus"
version = libs.versions.minecraft.get()
description = "Generator for Minecraft game data values"

java {
    withSourcesJar()
    withJavadocJar()

    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.register("generateData") {
    logger.warn("Mojang requires all source-code and mappings used to be governed by the Minecraft EULA.")
    logger.warn("Please read the Minecraft EULA located at https://account.mojang.com/documents/minecraft_eula.")
    logger.warn("In order to agree to the EULA you must create a file called eula.txt with the text 'eula=true'.")
    val eulaTxt = File("${rootProject.projectDir}/eula.txt")
    logger.warn("The file must be located at '${eulaTxt.absolutePath}'.")
    if ((eulaTxt.exists() && eulaTxt.readText(Charsets.UTF_8).equals("eula=true", true))
            || project.properties["eula"].toString().toBoolean()
            || System.getenv("EULA")?.toBoolean() == true
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
    dependsOn(project(":DataGenerator").tasks.getByName<JavaExec>("run") {
        args = arrayListOf(rootDir.resolve("src/main/resources").absolutePath)
    })
}

tasks.processResources.get().dependsOn("generateData")

publishData {
    addMainRepo("https://s01.oss.sonatype.org/service/local/")
    addSnapshotRepo("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

indra {
    javaVersions {
        target(21)
        testWith(21)
    }

    github("OneLiteFeatherNET", "MinestomDataGenerator") {
        ci(true)
        publishing(false)
    }
    apache2License()
    signWithKeyFromPrefixedProperties("onelitefeather")
    configurePublications {
        publishData.configurePublication(this)
        groupId = "net.onelitefeather.microtus"
        artifactId = "data"
        pom {
            developers {
                developer {
                    id.set("mworzala")
                    name.set("Matt Worzala")
                    email.set("matt@hollowcube.dev")
                }
                developer {
                    id.set("TheMode")
                }
                developer {
                    id.set("themeinerlp")
                    name.set("Phillipp Glanz")
                    email.set("p.glanz@madfix.me")
                }
                developer {
                    id.set("theEvilReaper")
                    name.set("Steffen Wonning")
                    email.set("steffenwx@gmail.com")
                }
            }
        }
    }
}