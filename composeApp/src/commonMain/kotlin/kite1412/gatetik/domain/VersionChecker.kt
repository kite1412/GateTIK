package kite1412.gatetik.domain

import kite1412.gatetik.BuildConfig

/**
 * Provides app version information and utilities for version comparison.
 */
interface VersionChecker {
    suspend fun getAppLatestVersion(): AppVersion?

    /**
     * Returns true if the latest version introduces a breaking change
     * compared to the currently running version.
     *
     * By default, this uses Semantic Versioning and treats a change in
     * the major version as a breaking change.
     */
    fun isBreakingChange(latestVersion: AppVersion): Boolean =
        semVerBreakingChange(latestVersion.versionCode)

    private fun semVerBreakingChange(code: String): Boolean {
        val latestMajor = code.substringBefore('.').toIntOrNull()
        val currentMajor = BuildConfig.VERSION.substringBefore('.').toIntOrNull()

        return latestMajor != null &&
            currentMajor != null &&
            latestMajor > currentMajor
    }
}

data class AppVersion(
    val tagName: String,
    val versionCode: String,
    val downloadUrl: String?
)