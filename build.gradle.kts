import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.shadow)
}

group = "com.funbiscuit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val javaMainClass = "MainKt"

    jvm {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass = javaMainClass
        }
        withJava()

        tasks.register<ShadowJar>("jvmShadowJar") { // create fat jar task
            val mainCompilation = compilations["main"]
            val jvmRuntimeConfiguration = mainCompilation
                .runtimeDependencyConfigurationName
                .let { project.configurations[it] }

            from(mainCompilation.output.allOutputs) // allOutputs == classes + resources
            configurations = listOf(jvmRuntimeConfiguration)
            archiveClassifier.set("fatjar")
            manifest.attributes("Main-Class" to javaMainClass)
        }
    }

    listOf(
        linuxX64(),
        linuxArm64(),
        mingwX64(),
        macosArm64(),
        macosX64(),
    ).forEach {
        it.binaries {
            executable {
                entryPoint = "main"
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.clikt)
        }

        jvmMain.dependencies {
        }

        nativeMain.dependencies {
        }
    }
}
