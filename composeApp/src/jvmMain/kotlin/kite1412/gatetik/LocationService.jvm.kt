package kite1412.gatetik

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UnsupportedJvmLocationService : LocationService {
    override fun observeLocationState(): Flow<LocationState> = flow {}
}