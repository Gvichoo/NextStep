package com.tbacademy.nextstep.data.repository.auth

import com.google.firebase.auth.FirebaseAuth
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.core.model.error.ApiError
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun getCurrentUserId(): Flow<Resource<String>> {
        return flow {
            try {
                val userId = firebaseAuth.uid

                if (userId != null) {
                    emit(Resource.Success(data = userId))
                } else {
                    emit(Resource.Error(error = ApiError.Unauthorized))

                }
            } catch (e: Exception) {
                emit(Resource.Error(error = e.toApiError()))
            }
        }
    }


}