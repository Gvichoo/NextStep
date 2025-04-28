package com.tbacademy.nextstep.data.repository.login

import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.repository.login.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val handleResponse: HandleResponse
) : LoginRepository {
    override fun login(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result.user != null))

            emit(Resource.Loading(false))

        } catch (e: Exception) {
            emit(Resource.Error(error = e.toApiError()))

            emit(Resource.Loading(false))
        }
        emit(Resource.Loading(false))
    }

    override fun logout(): Flow<Resource<Unit>> {
        return handleResponse.safeApiCall {
            firebaseAuth.signOut()
        }
    }
}