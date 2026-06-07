package kite1412.gatetik.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object BooleanPreferencesKey {
    val isDarkMode = booleanPreferencesKey("is_dark_theme")
    val isFirstLaunch = booleanPreferencesKey("is_first_launch")
    val isPollingEnabled = booleanPreferencesKey("is_polling_enabled")
}

object IntPreferencesKey {
    val pollingIntervalMs = intPreferencesKey("polling_interval_ms")
}

object JsonPreferencesKey {
    const val AUTH_SESSION = "auth_session"
}