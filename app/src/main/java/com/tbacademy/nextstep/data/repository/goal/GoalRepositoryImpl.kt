package com.tbacademy.nextstep.data.repository.goal

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.tbacademy.nextstep.data.common.mapper.toApiError
import com.tbacademy.nextstep.data.common.mapper.toDomain
import com.tbacademy.nextstep.data.common.mapper.toDomainWithComputedStatus
import com.tbacademy.nextstep.data.common.mapper.toDto
import com.tbacademy.nextstep.data.httpHelper.HandleResponse
import com.tbacademy.nextstep.data.remote.dto.GoalDto
import com.tbacademy.nextstep.domain.core.ApiError
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.model.Goal
import com.tbacademy.nextstep.domain.repository.goal.GoalRepository
import com.tbacademy.nextstep.presentation.model.MilestoneItem
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val handleResponse: HandleResponse
) : GoalRepository {
    override fun createGoal(goal: Goal): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading(loading = true))
        try {
            val currentUser = firebaseAuth.currentUser
            val userSnapshot: DocumentSnapshot? =
                currentUser?.let { firestore.collection("users").document(it.uid).get().await() }

            if (userSnapshot == null) {
                emit(Resource.Error(ApiError.Unauthorized))
                emit(Resource.Loading(loading = false))
                return@flow
            }

            val imageUrl = goal.imageUri?.let { uri ->
                val storageRef = firebaseStorage.reference.child(
                    "goal_images/${currentUser.uid}/${System.currentTimeMillis()}"
                )
                storageRef.putFile(uri).await()
                storageRef.downloadUrl.await().toString()
            }



            val goalRef = firestore.collection("goals").document()
            val goalId = goalRef.id
            val username: String? = userSnapshot.getString("username")


            if (username != null) {
                val goalDto: GoalDto = goal.toDto().copy(
                    authorId = currentUser.uid,
                    authorUsername = username,
                    id = goalId,
                    imageUrl = imageUrl ?: "",
                    type = PostType.GOAL
                )
                Log.d("UPLOAD_GOAL", "Goal to upload: $goalDto")
                // Upload goal to Firestore
                goalRef.set(goalDto).await()
                emit(Resource.Success(data = true))
            } else {
                emit(Resource.Error(ApiError.Unauthorized))
                return@flow
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
            Log.d("CREATE_GOAL", "GoalId: $e")
        } finally {
            emit(Resource.Loading(loading = false))
        }
    }

    override fun getGoalMilestones(goalId: String): Flow<Resource<Goal>> = flow {
        emit(Resource.Loading(true))
        try {
            val snapshot = firestore.collection("goals")
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
            val goalRef = firestore.collection("goals").document(goalId)

            // Update the milestones field
            goalRef.update("milestone", updatedGoalMilestones).await()

            emit(Resource.Success(true))
        } catch (e: Exception) {
            emit(Resource.Error(e.toApiError()))
        } finally {
            emit(Resource.Loading(false))
        }
    }




    override fun getUserGoals(userId: String): Flow<Resource<List<Goal>>> {
        return handleResponse.safeApiCall {
            val snapshot = firestore.collection("goals")
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
}

