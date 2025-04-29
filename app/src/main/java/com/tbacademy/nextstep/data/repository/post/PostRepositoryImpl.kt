package com.tbacademy.nextstep.data.repository.post

import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.httpHelper.HandleResponse.Companion.SORT_CREATED_AT
import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.model.PostType
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val handleResponse: HandleResponse

) : PostRepository {

    override suspend fun getGlobalPosts(): Flow<Resource<List<Post>>> {
        return handleResponse.safeApiCallWithUserId { userId ->

            val postSnapshot = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val postDtoList: List<PostDto> = postSnapshot.documents.mapNotNull {
                it.toObject(PostDto::class.java)?.copy(id = it.id)
            }

            val postIds: List<String> = postDtoList.map { it.id }

            val followedGoals = getFollowedGoals(userId)
            val reactions = getUserReactions(userId, postIds)

            mapPostDtos(postDtoList, reactions, followedGoals, userId)
        }
    }

    override suspend fun getFollowedPosts(): Flow<Resource<List<Post>>> {
        return handleResponse.safeApiCallWithUserId { userId ->

            // Get followed goals and users
            val followedGoals = getFollowedGoals(userId = userId)

            if (followedGoals.isEmpty()) {
                return@safeApiCallWithUserId emptyList<Post>()
            }

            // Query posts where authorId or goalId matches followed IDs
            val postQuery = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val filteredPosts = postQuery.documents.mapNotNull { doc ->
                val postDto = doc.toObject(PostDto::class.java)?.copy(id = doc.id)
                if (postDto != null && followedGoals.contains(postDto.goalId)) {
                    postDto
                } else null
            }

            val postIds = filteredPosts.map { it.id }
            val reactions = getUserReactions(userId, postIds)

            mapPostDtos(
                postDtos = filteredPosts,
                reactions = reactions,
                followedGoals = followedGoals,
                userId = userId
            )
        }
    }


    override fun createPost(
        title: String,
        description: String,
        imageUri: Uri,
        type: PostType,
        goalId: String,
    ): Flow<Resource<Unit>> {
        return handleResponse.withUserSnapshotFlow { userId, userSnapshot ->

            val username = userSnapshot.getString("username")
                ?: throw UserNotAuthenticatedException("User Not Found")

            val storageRef: StorageReference = firebaseStorage.reference
                .child("milestone_post_images/${userId}/${System.currentTimeMillis()}")
                storageRef.putFile(imageUri).await()
            val imageUrl = storageRef.downloadUrl.await().toString()

            val postRef = firestore.collection(POSTS_COLLECTION_PATH).document()
            val postId = postRef.id

            val postDto = PostDto(
                id = postId,
                authorId = userId,
                authorUsername = username,
                goalId = goalId,
                title = title,
                description = description,
                reactionCount = 0,
                commentCount = 0,
                imageUrl = imageUrl,
                createdAt = Timestamp.now(),
                type = type
            )

            postRef.set(postDto).await()
        }
    }

    override suspend fun getGoalPosts(goalId: String): Flow<Resource<List<Post>>> {
        return handleResponse.safeApiCall {
            val postSnapshot = firestore.collection(POSTS_COLLECTION_PATH)
                .whereEqualTo("goalId", goalId)
                .get()
                .await()

            val postDtoList =
                postSnapshot.mapNotNull { it.toObject(PostDto::class.java).copy(id = it.id) }

            postDtoList.map { it.toDomain() }
        }
    }

    override fun getPost(postId: String): Flow<Resource<Post>> {
        return handleResponse.safeApiCall {
            Log.d("POST_REPOSITORY", "POST ID: $postId")
            val snapshot = firestore
                .collection(POSTS_COLLECTION_PATH)
                .document(postId)
                .get()
                .await()

            Log.d("POST_REPOSITORY", "POST Snapshot: $snapshot")


            val postDto = snapshot.toObject(PostDto::class.java)
                ?: throw NotFoundException()

            postDto.copy(id = snapshot.id).toDomain()
        }
    }

    private suspend fun getFollowedGoals(userId: String): Set<String> {
        val snapshot = firestore.collection("goal_follows")
            .whereEqualTo("followerId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.getString("followedGoalId") }.toSet()
    }

    private suspend fun getUserReactions(
        userId: String,
        postIds: List<String>
    ): Map<String, ReactionType> {
        if (postIds.isEmpty()) return emptyMap()

        val snapshot = firestore.collection(REACTIONS_COLLECTION_PATH)
            .whereEqualTo(AUTHOR_ID_FIELD, userId)
            .whereIn(POST_ID_FIELD, postIds)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            val postId = doc.getString(POST_ID_FIELD) ?: return@mapNotNull null
            val type = doc.getString(REACTION_TYPE_FIELD) ?: return@mapNotNull null
            runCatching { ReactionType.valueOf(type) }.getOrNull()?.let {
                postId to it
            }
        }.toMap()
    }

    private fun mapPostDtos(
        postDtos: List<PostDto>,
        reactions: Map<String, ReactionType>,
        followedGoals: Set<String>,
        userId: String
    ): List<Post> {
        return postDtos.map { postDto ->
            val userReaction = reactions[postDto.id]
            val isOwnPost = postDto.authorId == userId
            val isUserFollowing = followedGoals.contains(postDto.goalId)

            postDto.toDomain().copy(
                userReaction = userReaction,
                isOwnPost = isOwnPost,
                isUserFollowing = isUserFollowing
            )
        }
    }

    companion object {
        const val POSTS_COLLECTION_PATH = "posts"
        const val REACTIONS_COLLECTION_PATH = "reactions"
        const val AUTHOR_ID_FIELD = "authorId"
        const val POST_ID_FIELD = "postId"
        const val REACTION_TYPE_FIELD = "type"
    }
}