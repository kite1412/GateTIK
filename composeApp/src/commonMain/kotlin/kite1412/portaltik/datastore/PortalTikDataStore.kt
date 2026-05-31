package kite1412.portaltik.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kite1412.portaltik.datastore.model.DataStoreAuthSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class PortalTikDataStore(
    private val dataStore: DataStore<Preferences>
) {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun observeAuthSession(): Flow<DataStoreAuthSession?> =
        dataStore.observeJsonPreference(JsonPreferencesKey.AUTH_SESSION)

    fun observeDarkMode(): Flow<Boolean?> =
        dataStore.observePreference(BooleanPreferencesKey.isDarkMode)

    fun observeFirstLaunch(): Flow<Boolean> =
        dataStore.observePreference(BooleanPreferencesKey.isFirstLaunch)
            .map { it ?: true }

    suspend fun getAuthSession(): DataStoreAuthSession? =
        dataStore.getJsonPreference(JsonPreferencesKey.AUTH_SESSION)

    suspend fun setAuthSession(data: DataStoreAuthSession) = dataStore.setJsonPreference(
        stringKey = JsonPreferencesKey.AUTH_SESSION,
        serializer = DataStoreAuthSession.serializer(),
        value = data
    )

    suspend fun deleteAuthSession() = dataStore.deletePreference(
        stringPreferencesKey(JsonPreferencesKey.AUTH_SESSION)
    )

    suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.setPreference(
            key = BooleanPreferencesKey.isDarkMode,
            value = darkMode
        )
    }

    suspend fun setFirstLaunch(value: Boolean) = dataStore.setPreference(
        key = BooleanPreferencesKey.isFirstLaunch,
        value = value
    )

    private suspend fun <T> DataStore<Preferences>.getPreference(
        key: Preferences.Key<T>
    ): T? = data.first()[key]

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

    private inline fun <reified T> DataStore<Preferences>.observeJsonPreference(stringKey: String): Flow<T?> =
        data.map { preferences ->
            preferences.getObject(stringKey)
        }

    private suspend inline fun <reified T> DataStore<Preferences>.getJsonPreference(stringKey: String): T? =
        data.first().run {
            getObject(stringKey)
        }

    private suspend fun <T> DataStore<Preferences>.setJsonPreference(
        stringKey: String,
        serializer: KSerializer<T>,
        value: T
    ) {
        setPreference(
            key = stringPreferencesKey(stringKey),
            value = json.encodeToString(serializer, value)
        )
    }

    private inline fun <reified T> Preferences.getObject(stringKey: String): T? {
        val jsonString =
            this[stringPreferencesKey(stringKey)]
                ?: return null

        return runCatching {
            json.decodeFromString<T>(jsonString)
        }.getOrNull()
    }
}