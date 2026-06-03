package kite1412.gatetik.domain.model

data class PaginatedListResult<T>(
    val data: List<T>,
    val pagination: Pagination
)

data class Pagination(
    val totalPage: Int,
    val perPage: Int,
    val currentPage: Int,
    val lastPage: Int,
    val from: Int,
    val to: Int
)