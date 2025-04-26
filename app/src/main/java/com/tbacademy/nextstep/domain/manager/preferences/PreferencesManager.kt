package com.tbacademy.nextstep.domain.manager.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {
    suspend fun <T> saveValue(key: PreferenceKey<T>, value: T)
    fun <T> readValue(key: PreferenceKey<T>): Flow<T?>
    suspend fun clear()
}