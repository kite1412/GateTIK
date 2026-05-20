package kite1412.portaltik.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PortalTikDataStore(
    private val dataStore: DataStore<Preferences>
) {
    fun observeDarkMode(): Flow<Boolean?> =
        dataStore.observePreference(BooleanPreferencesKey.isDarkMode)

    suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.setPreference(
            key = BooleanPreferencesKey.isDarkMode,
            value = darkMode
        )
    }

    private fun <T> DataStore<Preferences>.observePreference(
        key: Preferences.Key<T>
    ): Flow<T?> = data
        .map { preferences ->
            preferences[key]
        }

    private suspend fun <T> DataStore<Preferences>.setPreference(
        key: Preferences.Key<T>,
        value: T
    ) {
        edit { preferences ->
            preferences[key] = value
        }
    }

    private suspend fun <T> DataStore<Preferences>.deletePreference(
        key: Preferences.Key<T>
    ) {
        edit { preferences ->
            preferences.remove(key)
        }
    }
}