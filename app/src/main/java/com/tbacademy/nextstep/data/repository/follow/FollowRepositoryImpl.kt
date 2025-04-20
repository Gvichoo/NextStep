package com.tbacademy.nextstep.data.repository.follow

import android.content.res.Resources.NotFoundException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.common.mapper.toDto
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
import com.tbacademy.nextstep.data.remote.dto.FollowDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Follow
import com.tbacademy.nextstep.domain.model.FollowType
import com.tbacademy.nextstep.domain.repository.follow.FollowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: FirebaseHelper
): FollowRepository {
    override suspend fun createFollow(
        followType: FollowType,
        followingId: String
    ): Flow<Resource<Follow>> {
        return firebaseHelper.withUserIdFlow { userId ->
            val followRef = firestore.collection(FOLLOW_COLLECTION_KEY).document()

            val followDto = FollowDto(
                id = followRef.id,
                followerId = userId,
                followType = followType.toDto(),
                followedId = followingId
            )
            followRef.set(followDto).await()

            // Emit Data
            followDto.toDomain()
        }
    }

    override suspend fun checkIsUserFollowed(followedId: String): Flow<Resource<Boolean>> {
        return firebaseHelper.withUserIdFlow { userId ->
            val snapshot = firestore.collection(FOLLOW_COLLECTION_KEY)
                .whereEqualTo(FIELD_FOLLOWER_ID, userId)
                .whereEqualTo(FIELD_FOLLOWED_ID, followedId)
                .whereEqualTo(FIELD_FOLLOW_TYPE, FollowType.USER.name)
                .limit(1)
                .get()
                .await()

            !snapshot.isEmpty
        }
    }

    override suspend fun deleteFollow(followedId: String, followType: FollowType): Flow<Resource<Unit>> {
        return firebaseHelper.withUserIdFlow { userId ->
            val query = firestore.collection(FOLLOW_COLLECTION_KEY)
                .whereEqualTo(FIELD_FOLLOWER_ID, userId)
                .whereEqualTo(FIELD_FOLLOWED_ID, followedId)
                .whereEqualTo(FIELD_FOLLOW_TYPE, followType.toDto().name)
                .get()
                .await()

            val followDoc = query.documents.firstOrNull()?.reference ?: throw NotFoundException()
            followDoc.delete().await()
            Unit
        }
    }


    private companion object {
        const val FOLLOW_COLLECTION_KEY = "follows"
        const val FIELD_FOLLOWER_ID = "followerId"
        const val FIELD_FOLLOWED_ID = "followedId"
        const val FIELD_FOLLOW_TYPE = "followType"
    }
}