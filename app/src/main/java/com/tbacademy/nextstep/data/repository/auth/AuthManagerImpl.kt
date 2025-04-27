package com.tbacademy.nextstep.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.core.ApiError
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.manager.auth.AuthManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthManagerImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthManager {
    override fun getCurrentUserId(): String? = firebaseAuth.currentUser?.uid

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
}