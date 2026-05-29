package kite1412.portaltik

import kotlinx.coroutines.flow.Flow

interface LocationService {
    suspend fun getCurrentLocation(): Flow<Location>
}

data class Location(
    val latitude: Double,
    val longitude: Double
)