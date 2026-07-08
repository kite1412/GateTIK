import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.composeApp)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.components.resources)
    implementation(libs.compose.material3)
    implementation(libs.compose.uiToolingPreview)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.jcefmaven)
}

compose.desktop {
    application {
        mainClass = "kite1412.gatetik.desktop.MainKt"

        buildTypes.release.proguard {
            isEnabled.set(false)
        }

        run {
            jvmArgs(
                "--add-opens=java.base/java.lang=ALL-UNNAMED",
                "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
                "--add-opens=java.desktop/sun.java2d=ALL-UNNAMED",
                "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED"
            )
            if (System.getProperty("os.name").contains("Mac")) {
                jvmArgs(
                    "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
                    "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
                )
            }
        }

        jvmArgs(
            "-Dapple.awt.application.appearance=system"
        )

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            modules("jdk.unsupported")
            javaHome = System.getenv("JAVA_HOME")

            packageName = "Gate TIK"
            packageVersion = "2.3.1"

            macOS {
                iconFile.set(project.file("resource/desktop_icon.icns"))

                infoPlist {
                    extraKeysRawXml = """
                        <key>NSMicrophoneUsageDescription</key>
                        <string>Required for two-way audio communication.</string>
                    """.trimIndent()
                }
            }
            windows {
                iconFile.set(project.file("resource/desktop_icon.ico"))
            }
            linux {
                iconFile.set(project.file("resource/desktop_icon.png"))
            }
        }

        buildTypes.release {
            proguard {
                configurationFiles.from(
                    project.file("compose-desktop.pro")
                )
            }
        }
    }
}

afterEvaluate {
    tasks.withType<JavaExec> {
        jvmArgs(
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens=java.desktop/sun.java2d=ALL-UNNAMED",
            "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED"
        )
        if (System.getProperty("os.name").contains("Mac")) {
            jvmArgs(
                "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
                "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
            )
        }
    }
}