package com.tbacademy.nextstep.data.repository.goal

import android.util.Log
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tbacademy.core.model.Resource
import com.tbacademy.core.model.error.ApiError
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.common.mapper.toDomainWithComputedStatus
import com.tbacademy.nextstep.data.common.mapper.toDto
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.model.GoalStatus
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val handleResponse: HandleResponse
) : GoalRepository {
    override fun createGoal(goal: Goal): Flow<Resource<Boolean>> {
        return handleResponse.withUserSnapshotFlow { userId, userSnapshot ->
            val imageUrl = goal.imageUri?.let { uri ->
                val storageRef = firebaseStorage.reference.child(
                    "goal_images/$userId/${System.currentTimeMillis()}"
                )
                storageRef.putFile(uri).await()
                storageRef.downloadUrl.await().toString()
            }

            val goalRef = firestore.collection(COLLECTION_GOALS).document()
            val goalId = goalRef.id
            val username = userSnapshot.getString("username")
            val profilePictureUrl = userSnapshot.getString("profilePictureUrl")

            if (username != null) {
                val goalDto = goal.toDto().copy(
                    id = goalId,
                    authorId = userId,
                    authorUsername = username,
                    authorProfilePictureUrl = profilePictureUrl ?: "",
                    imageUrl = imageUrl.orEmpty()
                )
                goalRef.set(goalDto).await()
                true
            } else {
                Log.d("ERRORGOAL", "ERROR")
                throw FirebaseAuthInvalidUserException(
                    "ERROR_USER_NOT_FOUND",
                    "User profile is incomplete."
                )
            }
        }

    }

    override fun getGoalMilestones(goalId: String): Flow<Resource<Goal>> = flow {
        emit(Resource.Loading(true))
        try {
            val snapshot = firestore.collection(COLLECTION_GOALS)
                .document(goalId)
                .get()
                .await()

            val goalDto = snapshot.toObject(GoalDto::class.java)

            if (goalDto != null) {
                emit(Resource.Success(data = goalDto.toDomain()))
            } else {
                emit(Resource.Error(error = ApiError.NotFound))
            }

        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(false))
        }
    }


    override fun updateGoalMilestone(
        goalId: String,
        updatedGoalMilestones: List<MilestoneItem>
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(true))
        try {
            val goalRef = firestore.collection(COLLECTION_GOALS).document(goalId)

            // Update the milestones field
            goalRef.update("milestone", updatedGoalMilestones).await()

            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    override fun updateGoalStatus(goalStatus: GoalStatus, goalId: String): Flow<Resource<Unit>> {
        return handleResponse.safeApiCall {
            val goalRef = firestore.collection(COLLECTION_GOALS).document(goalId)
            goalRef.update("goalStatus", goalStatus).await()
        }
    }


    override fun getUserGoals(userId: String): Flow<Resource<List<Goal>>> {
        return handleResponse.safeApiCall {
            val snapshot = firestore.collection(COLLECTION_GOALS)
                .whereEqualTo("authorId", userId)
                .get()
                .await()

            val now = System.currentTimeMillis()

            val goalList: List<Goal> = snapshot.documents.mapNotNull { doc ->
                val goalDto = doc.toObject(GoalDto::class.java)?.copy(id = doc.id)
                goalDto?.toDomainWithComputedStatus(now)
            }

            goalList
        }
    }

    private companion object {
        const val COLLECTION_GOALS = "goals"
    }
}


