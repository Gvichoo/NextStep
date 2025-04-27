package com.tbacademy.nextstep.presentation.screen.main.goal

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.usecase.goal.GetUserGoalsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetGoalPostsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.base.launchEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.effect.GoalEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.event.GoalEvent
import com.tbacademy.nextstep.presentation.screen.main.goal.state.GoalState
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val getGoalPostsUseCase: GetGoalPostsUseCase
) : BaseViewModel<GoalState, GoalEvent, GoalEffect, Unit>(
    initialState = GoalState(),
    initialUiState = Unit
) {
    override fun onEvent(event: GoalEvent) {
        when (event) {
            is GoalEvent.FetchGoalPosts -> getPosts(goalId = event.goalId)
            is GoalEvent.CommentSelected -> launchEffect(effect = GoalEffect.OpenComments(postId = event.postId))
            is GoalEvent.CheckGoalAuthor -> onCheckAuthor(isOwnGoal = event.isOwnGoal)
            is GoalEvent.OpenCompleteGoalSheet -> updateState {
                copy(
                    isGoalCompleteBottomSheetVisible = true
                )
            }

            is GoalEvent.CloseCompleteGoalSheet -> updateState {
                copy(
                    isGoalCompleteBottomSheetVisible = false
                )
            }

            is GoalEvent.ProceedWithGoalCompletion -> onProceedWithGoalCompletion()
        }
    }

    private fun onCheckAuthor(isOwnGoal: Boolean) {
        viewModelScope.launch {
            updateState { copy(isOwnGoal = isOwnGoal) }
        }
    }

    private fun onProceedWithGoalCompletion() {
        state.value.goalTitle?.let {
            launchEffect(effect = GoalEffect.NavigateToCreateGoal(goalTitle = it))
        }
    }


    private fun getPosts(goalId: String) {
        viewModelScope.launch {
            getGoalPostsUseCase(goalId = goalId).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {
                        Log.d("GETPOSTS_ERROR", "${resource.error}")
                    }

                    is Resource.Loading -> updateState { copy(isLoading = resource.loading) }
                    is Resource.Success -> {
                        val posts = resource.data.map { it.toPresentation() }

                        updateState {
                            copy(posts = posts, goalTitle = posts.firstOrNull { it.type == PostType.GOAL }?.title)
                        }
                    }
                }
            }
        }
    }
}