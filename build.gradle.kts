plugins {
    `java-library`
    alias(libs.plugins.vanilla.gradle) apply false
    `maven-publish`
    signing
    alias(libs.plugins.nexuspublish)
    alias(libs.plugins.publisdata)
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


nexusPublishing {
    this.packageGroup.set("net.onelitefeather.microtus")

    repositories.sonatype {
        nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
        snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

        if (System.getenv("SONATYPE_USERNAME") != null) {
            username.set(System.getenv("SONATYPE_USERNAME"))
            password.set(System.getenv("SONATYPE_PASSWORD"))
        }
    }
}

publishing.publications.create<MavenPublication>("maven") {
    publishData.configurePublication(this)
    groupId = "net.onelitefeather.microtus"
    artifactId = "data"

    from(project.components["java"])

    pom {
        name.set("data")
        description.set("Minecraft game data values")
        url.set("https://github.com/OneLiteFeatherNET/MinestomDataGenerator")

        licenses {
            license {
                name.set("Apache 2.0")
                url.set("https://github.com/OneLiteFeatherNET/MinestomDataGenerator/blob/main/LICENSE")
            }
        }

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

        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/OneLiteFeatherNET/MinestomDataGenerator/issues")
        }

        scm {
            connection.set("scm:git:git://github.com/OneLiteFeatherNET/MinestomDataGenerator.git")
            developerConnection.set("scm:git:git@github.com:OneLiteFeatherNET/MinestomDataGenerator.git")
            url.set("https://github.com/OneLiteFeatherNET/MinestomDataGenerator")
            tag.set("HEAD")
        }

        ciManagement {
            system.set("Github Actions")
            url.set("https://github.com/OneLiteFeatherNET/MinestomDataGenerator/actions")
        }
    }
}

signing {
    isRequired = System.getenv("CI") != null

    val privateKey = System.getenv("GPG_PRIVATE_KEY")
    val keyPassphrase = System.getenv()["GPG_PASSPHRASE"]
    useInMemoryPgpKeys(privateKey, keyPassphrase)

    sign(publishing.publications)
}
