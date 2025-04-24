package com.tbacademy.nextstep.data.repository.reaction

import android.content.res.Resources.NotFoundException
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.ReactionDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.reaction.ReactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ReactionRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: HandleResponse
) : ReactionRepository {

    override suspend fun createReaction(
        postId: String,
        type: ReactionType
    ): Flow<Resource<Boolean>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->
            val reactionRef = firestore.collection(REACTION_COLLECTION_KEY).document()

            val reactionDto = ReactionDto(
                id = reactionRef.id,
                postId = postId,
                authorId = userId,
                type = type
            )
            reactionRef.set(reactionDto).await()

            true

        }
    }

    override suspend fun updateReaction(
        postId: String,
        newType: ReactionType
    ): Flow<Resource<Boolean>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->
            getReactionDoc(postId = postId, userId = userId).update(REACTION_TYPE_FIELD, newType)
                .await()
            true
        }
    }


    override suspend fun deleteReaction(postId: String): Flow<Resource<Boolean>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->
            getReactionDoc(postId = postId, userId = userId).delete().await()
            true
        }
    }

    // Helpers
    private suspend fun getReactionDoc(postId: String, userId: String): DocumentReference {
        val query = firestore.collection(REACTION_COLLECTION_KEY)
            .whereEqualTo(POST_ID_FIELD, postId)
            .whereEqualTo(AUTHOR_ID_FIELD, userId)
            .get()
            .await()

        return query.documents.firstOrNull()?.reference ?: throw NotFoundException()
    }

    private companion object {
        const val REACTION_COLLECTION_KEY = "reactions"
        const val REACTION_TYPE_FIELD = "type"
        const val POST_ID_FIELD = "postId"
        const val AUTHOR_ID_FIELD = "authorId"
    }
}