package com.tbacademy.nextstep.domain.usecase.user

import android.net.Uri
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UpdateUserImageUseCase {
    suspend operator fun invoke(imageUri: Uri): Flow<Resource<String>>
}

class UpdateUserImageUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
): UpdateUserImageUseCase {
    override suspend fun invoke(imageUri: Uri): Flow<Resource<String>> {
        return userRepository.updateUserImage(imageUri = imageUri)
    }
}