package kite1412.gatetik.data

import kite1412.gatetik.Logger
import kite1412.gatetik.PlatformType
import kite1412.gatetik.domain.AppVersion
import kite1412.gatetik.domain.VersionChecker
import kite1412.gatetik.getPlatform
import kite1412.gatetik.network.github.GitHubApi
import kite1412.gatetik.network.github.dto.response.GitHubRelease

class GitHubVersionChecker : VersionChecker {
    private val logTag = "GitHubVersionChecker"
    private val gitHubApi = GitHubApi()

    override suspend fun getAppLatestVersion(): AppVersion? = try {
        gitHubApi.getLatestRelease().toAppVersion()
    } catch (e: Exception) {
        e.printStackTrace()
        Logger.e(
            tag = logTag,
            message = "Failed to get latest release info from GitHub"
        )
        null
    }

    private fun GitHubRelease.toAppVersion(): AppVersion =
        AppVersion(
            tagName = tagName,
            versionCode = getVersionCode(tagName),
            downloadUrl = getBrowserDownloadUrl()
        )

    private fun getVersionCode(gitHubTagName: String): String =
        gitHubTagName.substringAfter('v')

    private fun GitHubRelease.getBrowserDownloadUrl(): String? {
        val platform = getPlatform()

        return assets.firstOrNull { asset ->
            when (platform.type) {
                PlatformType.MOBILE -> asset.name.endsWith(".apk")
                PlatformType.DESKTOP ->
                    when (platform.name.lowercase()) {
                        "windows" -> asset.name.endsWith(".msi")
                        "linux" -> asset.name.endsWith(".deb")
                        "macos" -> asset.name.endsWith(".dmg")
                        else -> false
                    }
            }
        }?.browserDownloadUrl
    }
}