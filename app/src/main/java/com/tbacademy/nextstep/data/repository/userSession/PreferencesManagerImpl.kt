package com.tbacademy.nextstep.data.repository.userSession

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.tbacademy.nextstep.domain.manager.preferences.PreferenceKey
import com.tbacademy.nextstep.domain.manager.preferences.PreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesManagerImpl @Inject constructor(
    private val datastore: DataStore<Preferences>
) : PreferencesManager {

    override suspend fun <T> saveValue(key: PreferenceKey<T>, value: T) {
        datastore.edit { preference ->
            val dsKey = createDataStoreKey(key)
            preference[dsKey] = value
        }
    }

    override fun <T> readValue(key: PreferenceKey<T>): Flow<T?> {
        return datastore.data.map { it[createDataStoreKey(key)] }
    }

    override suspend fun <T> clearByKey(key: PreferenceKey<T>) {
        datastore.edit { preference ->
            preference.remove(key = createDataStoreKey(key = key))
        }
    }

    override suspend fun clear() {
        datastore.edit {
            it.clear()
        }
    }

    private fun <T> createDataStoreKey(key: PreferenceKey<T>): Preferences.Key<T> {
        return when (key) {
            is PreferenceKey.StringKey -> stringPreferencesKey(key.keyName)
            is PreferenceKey.BooleanKey -> booleanPreferencesKey(key.keyName)
        } as Preferences.Key<T>
    }
}