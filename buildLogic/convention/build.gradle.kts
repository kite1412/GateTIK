import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    compileOnly(libs.kotlin.gp)
}

gradlePlugin {
    plugins {
        register("buildConfig") {
            id = "portaltik.buildconfig"
            implementationClass = "BuildConfigConventionPlugin"
        }
    }
}