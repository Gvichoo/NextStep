package com.tbacademy.nextstep.domain.manager.preferences

sealed class PreferenceKey<T>(
    val keyName: String,
) {
    class StringKey(keyName: String) :
        PreferenceKey<String>(keyName)

    class BooleanKey(keyName: String) :
        PreferenceKey<Boolean>(keyName)
}