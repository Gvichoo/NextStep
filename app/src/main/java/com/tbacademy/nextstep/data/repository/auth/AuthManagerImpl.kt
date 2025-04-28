package com.tbacademy.nextstep.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthManager {
    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}