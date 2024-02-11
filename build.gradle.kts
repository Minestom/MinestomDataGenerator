plugins {
    `java-library`
    alias(libs.plugins.vanilla.gradle) apply false
    `maven-publish`
    signing
    alias(libs.plugins.nexuspublish)
    alias(libs.plugins.publisdata)
}

group = "net.onelitefeather.microtus"
version = System.getenv("TAG_VERSION") ?: "${libs.versions.minecraft.get()}-dev"
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
    addMainRepo("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
    addSnapshotRepo("https://s01.oss.sonatype.org/content/repositories/snapshots")
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            publishData.configurePublication(this)
            groupId = "net.onelitefeather.microtus"
            artifactId = "data"
            version = publishData.getVersion()
            from(project.components["java"])

            pom {
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
                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
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
            }
        }
    }
    repositories {
        maven {
            url = uri(publishData.getRepository())
            credentials(PasswordCredentials::class)
            name = "sonatype"
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