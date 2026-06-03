package kite1412.gatetik.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DataStoreAuthSession(
    val token: String,
    val user: DataStoreUser
)
