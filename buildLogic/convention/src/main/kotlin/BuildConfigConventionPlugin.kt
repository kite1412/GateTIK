import kite1412.gatetik.BuildConfigExtension
import kite1412.gatetik.task.GenerateBuildConfigTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class BuildConfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val ext = target.extensions.create(
            /*name = */"buildConfig",
            /*type = */BuildConfigExtension::class.java,
            target
        )

        target.plugins.withId("org.jetbrains.kotlin.multiplatform") {
            val kmpExt = target.extensions.getByType<KotlinMultiplatformExtension>()
            val generateTask = target.tasks.register(
                /*name = */"generateBuildConfig",
                /*type = */GenerateBuildConfigTask::class.java
            ) {
                packageName.set(ext.packageName)
                stringFields.set(ext.stringFields)
                outputDir.set(target.layout.buildDirectory.dir("generated/buildconfig/commonMain"))
            }

            kmpExt.sourceSets.getByName("commonMain") {
                kotlin.srcDir(generateTask.map { it.outputDir })
            }

            target.tasks.named("compileKotlinMetadata") {
                dependsOn(generateTask)
            }
        }
    }
}