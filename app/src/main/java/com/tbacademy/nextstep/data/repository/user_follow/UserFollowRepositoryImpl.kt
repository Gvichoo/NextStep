package com.tbacademy.nextstep.data.repository.user_follow

import android.content.res.Resources.NotFoundException
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.UserFollowDto
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.model.UserFollow
import com.tbacademy.nextstep.domain.repository.user_follow.UserFollowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserFollowRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: HandleResponse
): UserFollowRepository {
    override suspend fun createUserFollow(followedId: String): Flow<Resource<UserFollow>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->
            val userFollowRef = firestore.collection(USER_FOLLOW_COLLECTION_KEY).document()

            val userFollowDto = UserFollowDto(
                id = userFollowRef.id,
                followerId = userId,
                followedUserId = followedId,
            )
            userFollowRef.set(userFollowDto)

            // Emit Data
            userFollowDto.toDomain()
        }
    }

    override suspend fun deleteUserFollow(followedId: String): Flow<Resource<Unit>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->
            val query = firestore.collection(USER_FOLLOW_COLLECTION_KEY)
                .whereEqualTo(FIELD_FOLLOWER_ID, userId)
                .whereEqualTo(FIELD_FOLLOWED_USER_ID, followedId)
                .get()
                .await()

            val followDoc = query.documents.firstOrNull()?.reference ?: throw NotFoundException()
            followDoc.delete().await()
        }
    }

    private companion object {
        const val USER_FOLLOW_COLLECTION_KEY = "user_follows"
        const val FIELD_FOLLOWER_ID = "followerId"
        const val FIELD_FOLLOWED_USER_ID = "followedUserId"
    }
}