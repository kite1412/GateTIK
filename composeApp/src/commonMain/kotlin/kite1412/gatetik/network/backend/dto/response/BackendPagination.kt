package kite1412.gatetik.network.backend.dto.response

import kite1412.gatetik.domain.model.Pagination
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BackendPagination(
    val total: Int,
    @SerialName("per_page")
    val perPage: Int,
    @SerialName("current_page")
    val currentPage: Int,
    @SerialName("last_page")
    val lastPage: Int,
    val from: Int?,
    val to: Int?
) {
    fun toModel() = Pagination(
        totalPages = total,
        perPage = perPage,
        currentPage = currentPage,
        lastPage = lastPage,
        from = from ?: 0,
        to = to ?: 0
    )
}
