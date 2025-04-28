package com.tbacademy.nextstep.domain.repository.post

import android.net.Uri
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.model.PostType
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getGlobalPosts(): Flow<Resource<List<Post>>>
    suspend fun getFollowedPosts(): Flow<Resource<List<Post>>>

    fun createPost(
        title: String,
        description: String,
        imageUri: Uri,
        type: PostType,
        goalId: String,
        ): Flow<Resource<Unit>>

    suspend fun getGoalPosts(goalId: String): Flow<Resource<List<Post>>>
    fun getPost(postId: String): Flow<Resource<Post>>

}