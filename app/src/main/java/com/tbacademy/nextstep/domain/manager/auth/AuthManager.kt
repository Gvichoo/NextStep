package com.tbacademy.nextstep.domain.manager.auth

interface AuthManager {
    fun getCurrentUserId(): String?
    fun isUserLoggedIn(): Boolean
}