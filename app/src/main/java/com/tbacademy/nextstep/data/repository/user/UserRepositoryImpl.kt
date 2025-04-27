package com.tbacademy.nextstep.data.repository.user

import android.content.res.Resources.NotFoundException
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.UserDto
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.User
import com.tbacademy.nextstep.domain.repository.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val handleResponse: HandleResponse
) : UserRepository {
    override fun getUserInfo(userId: String): Flow<Resource<User>> {
        return handleResponse.safeApiCallWithUserId { authId ->
            val userSnapshot = firestore.collection(USER_COLLECTION_KEY)
                .document(userId)
                .get()
                .await()

            val userDto = userSnapshot.toObject(UserDto::class.java)
                ?: throw NotFoundException()

            val isOwnUser = userDto.uid == authId

            val isUserFollowed = if (!isOwnUser)
            {
                firestore.collection(USER_FOLLOWS_COLLECTION_KEY)
                    .whereEqualTo(FIELD_FOLLOWER_ID, authId)
                    .whereEqualTo(FIELD_FOLLOWED_ID, userId)
                    .get()
                    .await()
                    .isEmpty.not()
            } else false

            Log.d("USER_STATE_REPOSITORY", "$isUserFollowed")

            userDto.toDomain().copy(isOwnUser = isOwnUser, isUserFollowed = isUserFollowed)
        }
    }

    override fun searchUsers(query: String): Flow<Resource<List<User>>> {
        return handleResponse.safeApiCall {
            val usersSnapshot = firestore.collection(USER_COLLECTION_KEY)
                .orderBy(FIELD_USERNAME_LOWER)
                .startAt(query.lowercase())
                .endAt(query.lowercase() + "\uf8ff")
                .get()
                .await()

            val userDtoList = usersSnapshot.documents.mapNotNull {
                it.toObject(UserDto::class.java)?.copy(uid = it.id)
            }

            userDtoList.map { it.toDomain() }
        }
    }

    override fun updateUserImage(imageUri: Uri): Flow<Resource<String>> {
        return handleResponse.safeApiCallWithUserId { userId ->
            val storageRef = firebaseStorage.reference
                .child("user_images/${userId}.jpg")

            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await().toString()

            firestore.collection(USER_COLLECTION_KEY)
                .document(userId)
                .update(FIELD_PROFILE_PICTURE_URL, downloadUrl)
                .await()

            downloadUrl
        }
    }

    private companion object {
        const val USER_COLLECTION_KEY = "users"
        const val FIELD_USERNAME_LOWER = "usernameLower"
        const val FIELD_FOLLOWER_ID = "followerId"
        const val FIELD_FOLLOWED_ID = "followedUserId"
        const val FIELD_PROFILE_PICTURE_URL = "profilePictureUrl"
        const val USER_FOLLOWS_COLLECTION_KEY = "user_follows"
    }
}