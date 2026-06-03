package kite1412.gatetik.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object BooleanPreferencesKey {
    val isDarkMode = booleanPreferencesKey("is_dark_theme")
    val isFirstLaunch = booleanPreferencesKey("is_first_launch")
}

object JsonPreferencesKey {
    const val AUTH_SESSION = "auth_session"
}