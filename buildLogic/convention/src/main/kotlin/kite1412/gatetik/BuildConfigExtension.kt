package kite1412.gatetik

import org.gradle.api.provider.MapProperty
import java.util.Properties

abstract class BuildConfigExtension(
    private val localProperties: Properties
) {
    abstract var packageName: String
    abstract val stringFields: MapProperty<String, String>

    fun buildConfigField(name: String) {
        val value = localProperties.getProperty(name) ?: System.getenv(name)
        requireNotNull(value) { "$name not found in local.properties or environment variables" }

        stringFields.put(name, value)
    }
}