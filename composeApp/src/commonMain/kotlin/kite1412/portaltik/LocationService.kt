package kite1412.portaltik

import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun observeLocationState(): Flow<LocationState>
}

sealed interface LocationState {
    object Loading : LocationState
    object Unavailable : LocationState
    data class Available(
        val currentLocation: Location
    ) : LocationState
}

data class Location(
    val latitude: Double,
    val longitude: Double
)