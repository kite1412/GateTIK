package kite1412.portaltik

import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import java.util.Properties

abstract class BuildConfigExtension(private val project: Project) {
    abstract var packageName: String
    abstract val stringFields: MapProperty<String, String>

    private val localProperties: Properties by lazy {
        val props = Properties()
        val localProperties = project.rootProject.file("local.properties")
        if (localProperties.exists()) {
            localProperties.inputStream().use { props.load(it) }
        }
        props
    }

    fun buildConfigField(name: String) {
        val value = localProperties.getProperty(name) ?: System.getenv(name)
        requireNotNull(value) { "$name not found in local.properties or environment variables" }

        stringFields.put(name, value)
    }
}