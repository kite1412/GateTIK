package kite1412.portaltik

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UnsupportedJvmLocationService : LocationService {
    override suspend fun getCurrentLocation(): Flow<Location> = flow {}
}