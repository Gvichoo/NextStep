package com.tbacademy.nextstep.data.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKey{
    val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
    val LANGUAGE_KEY = stringPreferencesKey("language_key")
}
