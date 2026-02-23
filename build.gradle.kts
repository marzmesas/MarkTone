import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    kotlin("jvm") version "2.1.20"
    id("org.jetbrains.intellij.platform") version "2.11.0"
}

group = "io.marktone"
version = "0.2.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2")
        bundledPlugin("org.intellij.plugins.markdown")
        testFramework(TestFrameworkType.Platform)
    }

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

intellijPlatform {
    pluginConfiguration {
        version = project.version.toString()

        ideaVersion {
            sinceBuild = "242"
        }
    }
}

tasks {
    runIde {
        jvmArgs("-Xmx2g")
    }

    test {
        useJUnitPlatform()
    }
}
