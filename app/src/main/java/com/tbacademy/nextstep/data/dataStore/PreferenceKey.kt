package com.tbacademy.nextstep.data.dataStore

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tbacademy.nextstep.domain.manager.preferences.PreferenceKey

object PreferenceKey{
    val KEY_REMEMBER_ME = booleanPreferencesKey("remember_me")
    val LANGUAGE_KEY = stringPreferencesKey("language_key")
    val KEY_THEME_MODE: PreferenceKey.StringKey = PreferenceKey.StringKey(keyName = "theme_mode")
}
