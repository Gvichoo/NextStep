package com.tbacademy.nextstep.data.repository.follow

import android.content.res.Resources.NotFoundException
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
import com.tbacademy.nextstep.data.remote.dto.FollowDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.GoalFollow
import com.tbacademy.nextstep.domain.repository.follow.GoalFollowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalFollowRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: FirebaseHelper
): GoalFollowRepository {
    override suspend fun createGoalFollow(
        followingId: String
    ): Flow<Resource<GoalFollow>> {
        return firebaseHelper.withUserIdFlow { userId ->
            val followRef = firestore.collection(GOAL_FOLLOW_COLLECTION_KEY).document()

            val followDto = FollowDto(
                id = followRef.id,
                followerId = userId,
                followedId = followingId
            )
            followRef.set(followDto).await()

            // Emit Data
            followDto.toDomain()
        }
    }

    override suspend fun deleteGoalFollow(followedId: String): Flow<Resource<Unit>> {
        return firebaseHelper.withUserIdFlow { userId ->
            val query = firestore.collection(GOAL_FOLLOW_COLLECTION_KEY)
                .whereEqualTo(FIELD_FOLLOWER_ID, userId)
                .whereEqualTo(FIELD_FOLLOWED_ID, followedId)
                .get()
                .await()

            val followDoc = query.documents.firstOrNull()?.reference ?: throw NotFoundException()
            followDoc.delete().await()
            Unit
        }
    }

    private companion object {
        const val GOAL_FOLLOW_COLLECTION_KEY = "goal_follows"
        const val FIELD_FOLLOWER_ID = "followerId"
        const val FIELD_FOLLOWED_ID = "followedId"
    }
}