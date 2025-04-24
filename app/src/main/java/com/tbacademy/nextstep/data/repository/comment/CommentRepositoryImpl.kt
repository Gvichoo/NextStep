package com.tbacademy.nextstep.data.repository.comment

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.CommentDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Comment
import com.tbacademy.nextstep.domain.repository.comment.CommentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: HandleResponse
) : CommentRepository {
    override suspend fun getComments(postId: String): Flow<Resource<List<Comment>>> {
        return flow {
            emit(Resource.Loading(loading = true))
            try {
                val commentsSnapshot = firestore.collection("comments")
                    .orderBy("createdAt", Query.Direction.DESCENDING)
                    .whereEqualTo("postId", postId)
                    .get()
                    .await()

                val commentDtoList: List<CommentDto> = commentsSnapshot.documents.mapNotNull {
                    it.toObject(CommentDto::class.java)?.copy(id = it.id)
                }
                emit(Resource.Success(data = commentDtoList.map { it.toDomain() }))
            } catch (e: Exception) {
                emit(Resource.Error(error = e.toApiError()))
            } finally {
                emit(Resource.Loading(loading = false))
            }
        }
    }

    override suspend fun createComment(postId: String, text: String): Flow<Resource<Comment>> {
        return firebaseHelper.withUserSnapshotFlow { userId, userSnapshot ->
            val commentRef = firestore.collection(COMMENT_COLLECTION_KEY).document()
            val username =
                userSnapshot.getString(USERNAME_FIELD_KEY) ?: throw FirebaseNoSignedInUserException(
                    "Username can't be found"
                )
            val authorProfilePictureUrl = userSnapshot.getString("profilePictureUrl")

            val commentDto = CommentDto(
                id = commentRef.id,
                postId = postId,
                authorId = userId,
                authorUsername = username,
                authorProfilePictureUrl = authorProfilePictureUrl,
                text = text
            )

            commentRef.set(commentDto).await()
            commentDto.toDomain()
        }
    }

    companion object {
        const val COMMENT_COLLECTION_KEY = "comments"
        const val USERNAME_FIELD_KEY = "username"
    }
}