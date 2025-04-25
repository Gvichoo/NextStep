package com.tbacademy.nextstep.domain.repository.post

import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    suspend fun getGlobalPosts(): Flow<Resource<List<Post>>>
    suspend fun getFollowedPosts(): Flow<Resource<List<Post>>>
    suspend fun getGoalPosts(goalId: String): Flow<Resource<List<Post>>>
    suspend fun getPost(postId: String): Flow<Resource<Post>>
}