package com.tbacademy.nextstep.domain.usecase.post

import android.net.Uri
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface CreatePostUseCase {
    suspend operator fun invoke(
        title: String,
        description: String,
        imageUri: Uri,
        type: PostType,
        goalId: String,
    ): Flow<Resource<Unit>>
}

class CreatePostUseCaseImpl @Inject constructor(
    private val postRepository: PostRepository
) : CreatePostUseCase {
    override suspend fun invoke(
        title: String,
        description: String,
        imageUri: Uri,
        type: PostType,
        goalId: String,
    ): Flow<Resource<Unit>> {
        return postRepository.createPost(
            title = title,
            description = description,
            imageUri = imageUri,
            type = type,
            goalId = goalId
        )
    }
}


