package kite1412.portaltik.task

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateBuildConfigTask : DefaultTask() {
    @get:Input
    abstract val packageName: Property<String>

    @get:Input
    abstract val stringFields: MapProperty<String, String>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val packageNameValue = packageName.get()
        val outputDirectory = outputDir.get().asFile

        outputDirectory.deleteRecursively()
        outputDirectory.mkdirs()

        val fileContent = buildString {
            appendLine("package $packageNameValue")
            appendLine()
            appendLine("object BuildConfig {")

            stringFields.get().forEach { (name, value) ->
                appendLine("    const val $name: String = \"$value\"")
            }

            appendLine("}")
        }

        val packagePath = packageNameValue.replace('.', '/')
        val targetDir = File(outputDirectory, packagePath)
        targetDir.mkdirs()

        File(targetDir, "BuildConfig.kt").writeText(fileContent)
    }
}