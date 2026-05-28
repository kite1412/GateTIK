package kite1412.portaltik

interface LocationService {
    suspend fun getCurrentLocation(): Location
}

data class Location(
    val latitude: Double,
    val longitude: Double
)