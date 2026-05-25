package kite1412.portaltik.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object BooleanPreferencesKey {
    val isDarkMode = booleanPreferencesKey("is_dark_theme")
}

object StringPreferencesKey {
    val token = stringPreferencesKey("token")
}

object JsonPreferencesKey {
    const val USER = "user"
}