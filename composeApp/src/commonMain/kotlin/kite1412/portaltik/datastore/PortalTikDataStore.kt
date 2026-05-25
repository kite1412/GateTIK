package kite1412.portaltik.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kite1412.portaltik.datastore.extension.toDataStoreUser
import kite1412.portaltik.datastore.model.DataStoreUser
import kite1412.portaltik.model.User
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

    fun observeUser(): Flow<User?> =
        dataStore
            .observeJsonPreference<DataStoreUser>(JsonPreferencesKey.USER)
            .map { it?.toModel() }

    suspend fun setUser(user: User) = dataStore.setJsonPreference(
        stringKey = JsonPreferencesKey.USER,
        serializer = DataStoreUser.serializer(),
        value = user.toDataStoreUser()
    )

    suspend fun deleteUser() {
        dataStore.deletePreference(stringPreferencesKey(JsonPreferencesKey.USER))
    }

    fun observeDarkMode(): Flow<Boolean?> =
        dataStore.observePreference(BooleanPreferencesKey.isDarkMode)

    suspend fun setDarkMode(darkMode: Boolean) {
        dataStore.setPreference(
            key = BooleanPreferencesKey.isDarkMode,
            value = darkMode
        )
    }

    suspend fun getToken(): String? = dataStore.getPreference(StringPreferencesKey.token)

    suspend fun setToken(token: String) = dataStore.setPreference(
        key = StringPreferencesKey.token,
        value = token
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
            val jsonString =
                preferences[stringPreferencesKey(stringKey)]
                    ?: return@map null

            runCatching {
                json.decodeFromString<T>(jsonString)
            }.getOrNull()
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
}