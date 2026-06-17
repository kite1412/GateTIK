package kite1412.gatetik.network.github.dto.response

import kite1412.gatetik.network.github.dto.model.GitHubAsset
import kite1412.gatetik.network.github.dto.model.GitHubUser
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRelease(
    val url: String,
    @SerialName("assets_url")
    val assetsUrl: String,
    @SerialName("upload_url")
    val uploadUrl: String,
    @SerialName("html_url")
    val htmlUrl: String,
    val id: Long,
    val author: GitHubUser,
    @SerialName("node_id")
    val nodeId: String,
    @SerialName("tag_name")
    val tagName: String,
    @SerialName("target_commitish")
    val targetCommitish: String,
    val name: String,
    val draft: Boolean,
    val immutable: Boolean,
    val prerelease: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("published_at")
    val publishedAt: String?,
    val assets: List<GitHubAsset>,
    @SerialName("tarball_url")
    val tarballUrl: String,
    @SerialName("zipball_url")
    val zipballUrl: String,
    val body: String
)
