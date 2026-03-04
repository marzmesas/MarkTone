import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.11.0"
}

group = "io.marktone"
version = "0.4.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2025.2.6.1")
        bundledPlugin("org.intellij.plugins.markdown")
        plugin("com.chrisrm.idea.MaterialThemeUI", "10.5.0")
        plugin("com.thvardhan.gradianto", "5.5")
        testFramework(TestFrameworkType.JUnit5)
    }

    testImplementation(kotlin("test"))
    testImplementation("org.opentest4j:opentest4j:1.3.0")
    testRuntimeOnly("junit:junit:4.13.2")
}

kotlin {
    jvmToolchain(17)
}

intellijPlatform {
    pluginConfiguration {
        version = project.version.toString()

        ideaVersion {
            sinceBuild = "233"
        }
    }
}

tasks {
    runIde {
        jvmArgs("-Xmx2g")
    }

    register<JavaExec>("runPyCharm") {
        group = "intellij platform"
        description = "Run plugin in local PyCharm installation"

        val sandboxDir = layout.buildDirectory.dir("pycharm-sandbox")
        val pyCharmPath = "/Applications/PyCharm.app/Contents"

        classpath = files("$pyCharmPath/lib/app.jar")
        mainClass.set("com.intellij.idea.Main")

        jvmArgs(
            "-Xmx2g",
            "-Didea.paths.selector=MarkTonePyCharmSandbox",
            "-Dplugin.path=${layout.buildDirectory.dir("idea-sandbox/plugins/MarkTone").get().asFile}",
            "-Didea.system.path=${sandboxDir.get().asFile}/system",
            "-Didea.config.path=${sandboxDir.get().asFile}/config",
            "-Didea.log.path=${sandboxDir.get().asFile}/log",
        )

        dependsOn("prepareSandbox")
    }

    test {
        useJUnitPlatform()
    }
}
