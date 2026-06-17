package kite1412.gatetik.network.github.dto.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubAsset(
    val url: String,
    val id: Long,
    @SerialName("node_id")
    val nodeId: String,
    val name: String,
    val label: String? = null,
    val uploader: GitHubUser,
    @SerialName("content_type")
    val contentType: String,
    val state: String,
    val size: Long,
    val digest: String? = null,
    @SerialName("download_count")
    val downloadCount: Int,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("browser_download_url")
    val browserDownloadUrl: String
)