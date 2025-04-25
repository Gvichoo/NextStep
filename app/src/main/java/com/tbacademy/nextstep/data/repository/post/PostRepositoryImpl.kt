package com.tbacademy.nextstep.data.repository.post

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.httpHelper.HandleResponse.Companion.SORT_CREATED_AT
import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.MilestonePost
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: HandleResponse,
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) : PostRepository {

    override suspend fun getGlobalPosts(): Flow<Resource<List<Post>>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->

            val postSnapshot = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val postDtos = postSnapshot.documents.mapNotNull {
                it.toObject(PostDto::class.java)?.copy(id = it.id)
            }

            val postIds = postDtos.map { it.id }

            val followedGoals = getFollowedGoals(userId)
            val reactions = getUserReactions(userId, postIds)

            mapPostDtos(postDtos, reactions, followedGoals, userId)
        }
    }

    override suspend fun getFollowedPosts(): Flow<Resource<List<Post>>> {
        return firebaseHelper.safeApiCallWithUserId { userId ->

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

    override fun createMilestonePost(milestonePost: MilestonePost): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(loading = true))
        try {
            val currentUser = firebaseAuth.currentUser
            if (currentUser == null) {
                emit(Resource.Error(ApiError.Unauthorized))
                return@flow
            }

            val userSnapshot: DocumentSnapshot? = firestore.collection("users")
                .document(currentUser.uid).get().await()

            if (userSnapshot == null || !userSnapshot.exists()) {
                emit(Resource.Error(ApiError.Unauthorized))
                return@flow
            }

            val username = userSnapshot.getString("username") ?: return@flow emit(Resource.Error(ApiError.Unauthorized))

            val imageUrl = try {
                milestonePost.imageUri?.let { uri ->
                    val storageRef = firebaseStorage.reference
                        .child("milestone_post_images/${currentUser.uid}/${System.currentTimeMillis()}")
                    storageRef.putFile(uri).await()
                    storageRef.downloadUrl.await().toString()
                }
            } catch (e: Exception) {
                null
            }

            // ✅ Generate a new post ID from the 'posts' collection
            val postRef = firestore.collection(POSTS_COLLECTION_PATH).document()
            val milestonePostId = postRef.id

            val post = Post(
                id = milestonePostId,
                authorId = currentUser.uid,
                authorUsername = username,
                goalId = milestonePost.goalId,
                title = milestonePost.title,
                description = milestonePost.description,
                reactionCount = 0,
                commentCount = 0,
                imageUrl = imageUrl,
                createdAt = Date(),
                type = PostType.GOAL
            )

            // ✅ Save only to the 'posts' collection
            postRef.set(post).await()

            emit(Resource.Success(data = true))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(loading = false))
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