import org.spongepowered.gradle.plugin.config.PluginLoaders
import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform
import org.spongepowered.plugin.metadata.model.PluginDependency

plugins {
    `maven-publish`
    `java-library`
    id("org.spongepowered.gradle.plugin") version "2.1.1"
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

group = "eu.crushedpixel.sponge"
version = "0.3.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
}

minecraft {
    version("1.20.2")
    platform(MinecraftPlatform.SERVER)
    injectRepositories(false)
}

sponge {
    apiVersion("11.0.0-SNAPSHOT")
    license("NO_LICENCE_YET")
    plugin("packetgate") {
	    loader {
	        name(PluginLoaders.JAVA_PLAIN)
	        version("1.0")
	    }
        displayName("PacketGate")
        entrypoint("eu.crushedpixel.sponge.packetgate.plugin.PluginPacketGate")
        description("Sponge library to manipulate incoming and outgoing Packets. ")
        links {
            homepage("https://github.com/Quutio/PacketGate")
            source("https://github.com/Quutio/PacketGate")
            issues("https://github.com/Quutio/PacketGate/issues")
        }
        contributor("CrushedPixel") {
            description("Lead developer")
        }
        contributor("Masa") {
            description("API11 port")
        }
        dependency("spongeapi") {
            loadOrder(PluginDependency.LoadOrder.AFTER)
            optional(false)
        }
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["MixinConfigs"] = "mixins.packetgate.json"
    }
}

val javaTarget = 17 // Sponge targets a minimum of Java 17
java {
    sourceCompatibility = JavaVersion.toVersion(javaTarget)
    targetCompatibility = JavaVersion.toVersion(javaTarget)
    if (JavaVersion.current() < JavaVersion.toVersion(javaTarget)) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(javaTarget))
    }
}

tasks.withType(JavaCompile::class).configureEach {
    options.apply {
        encoding = "utf-8" // Consistent source file encoding
        if (JavaVersion.current().isJava10Compatible) {
            release.set(javaTarget)
        }
    }
}

// Make sure all tasks which produce archives (jar, sources jar, javadoc jar, etc) produce more consistent output
tasks.withType(AbstractArchiveTask::class).configureEach {
    isReproducibleFileOrder = true
    isPreserveFileTimestamps = false
}