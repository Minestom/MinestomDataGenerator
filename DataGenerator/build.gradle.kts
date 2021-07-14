group = "net.minestom"

plugins {
    java
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // Logging
    implementation("org.apache.logging.log4j:log4j-core:2.14.0")
    // SLF4J is the base logger for most libraries, therefore we can hook it into log4j2.
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.0")
    implementation("com.google.code.gson:gson:2.8.6")

    implementation(files("../Deobfuscator/deobfuscated_jars/deobfu_1.17.1.jar"))

}

val mcVersion = project.properties["mcVersion"].toString()

application {
    mainClass.set("net.minestom.datagen.DataGen")
}