package kite1412.portaltik.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

object BooleanPreferencesKey {
    val isDarkMode = booleanPreferencesKey("is_dark_theme")
}