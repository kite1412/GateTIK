package kite1412.gatetik.network.github

import io.ktor.client.call.body
import io.ktor.client.request.get
import kite1412.gatetik.BuildConfig
import kite1412.gatetik.network.domain.ktorHttpClient
import kite1412.gatetik.network.github.dto.response.GitHubRelease

class GitHubApi {
    suspend fun getLatestRelease(): GitHubRelease =
        ktorHttpClient.get(
            urlString = BuildConfig.GITHUB_LATEST_RELEASE_URL
        )
            .body<GitHubRelease>()
}