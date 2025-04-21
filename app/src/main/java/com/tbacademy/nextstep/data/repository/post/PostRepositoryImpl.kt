package com.tbacademy.nextstep.data.repository.post

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper
import com.tbacademy.nextstep.data.httpHelper.FirebaseHelper.Companion.SORT_CREATED_AT
import com.tbacademy.nextstep.data.remote.dto.PostDto
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Post
import com.tbacademy.nextstep.domain.model.ReactionType
import com.tbacademy.nextstep.domain.repository.post.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseHelper: FirebaseHelper
) : PostRepository {

    override suspend fun getGlobalPosts(): Flow<Resource<List<Post>>> {
        return firebaseHelper.withUserIdFlow { userId ->

            val postSnapshot = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val postDtos = postSnapshot.documents.mapNotNull {
                it.toObject(PostDto::class.java)?.copy(id = it.id)
            }

            val postIds = postDtos.map { it.id }

            val (followedGoals, followedUsers) = getUserFollows(userId)
            val reactions = getUserReactions(userId, postIds)

            mapPostDtos(postDtos, reactions, followedGoals, followedUsers, userId)
        }
    }

    override suspend fun getFollowedPosts(): Flow<Resource<List<Post>>> {
        return firebaseHelper.withUserIdFlow { userId ->

            // Get followed goals and users
            val (followedGoals, followedUsers) = getUserFollows(userId)

            if (followedGoals.isEmpty() && followedUsers.isEmpty()) {
                return@withUserIdFlow emptyList<Post>()
            }

            // Query posts where authorId or goalId matches followed IDs
            val postQuery = firestore.collection(POSTS_COLLECTION_PATH)
                .orderBy(SORT_CREATED_AT, Query.Direction.DESCENDING)
                .get()
                .await()

            val filteredPosts = postQuery.documents.mapNotNull { doc ->
                val postDto = doc.toObject(PostDto::class.java)?.copy(id = doc.id)
                if (postDto != null &&
                    (followedUsers.contains(postDto.authorId) || followedGoals.contains(postDto.goalId))
                ) {
                    postDto
                } else null
            }

            val postIds = filteredPosts.map { it.id }
            val reactions = getUserReactions(userId, postIds)

            mapPostDtos(filteredPosts, reactions, followedGoals, followedUsers, userId)
        }
    }


    private suspend fun getUserFollows(userId: String): Pair<Set<String>, Set<String>> {
        val snapshot = firestore.collection("follows")
            .whereEqualTo("followerId", userId)
            .get()
            .await()

        val goals = mutableSetOf<String>()
        val users = mutableSetOf<String>()

        snapshot.documents.forEach { doc ->
            val type = doc.getString("followType")
            val id = doc.getString("followedId")
            when (type) {
                "GOAL" -> id?.let { goals.add(it) }
                "USER" -> id?.let { users.add(it) }
            }
        }
        return goals to users
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
        followedUsers: Set<String>,
        userId: String
    ): List<Post> {
        return postDtos.map { postDto ->
            val userReaction = reactions[postDto.id]
            val isOwnPost = postDto.authorId == userId

            postDto.toDomain().copy(
                userReaction = userReaction,
                isOwnPost = isOwnPost
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