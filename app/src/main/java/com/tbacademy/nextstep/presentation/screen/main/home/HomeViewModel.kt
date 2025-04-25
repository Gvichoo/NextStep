package com.tbacademy.nextstep.presentation.screen.main.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.onError
import com.tbacademy.nextstep.domain.usecase.goal_follow.CreateGoalFollowUseCase
import com.tbacademy.nextstep.domain.usecase.goal_follow.DeleteGoalFollowUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetFollowedPostsUseCase
import com.tbacademy.nextstep.domain.usecase.post.GetPostsUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.CreateReactionUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.DeleteReactionUseCase
import com.tbacademy.nextstep.domain.usecase.reaction.UpdateReactionUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.extension.adjustCount
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toDomain
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation
import com.tbacademy.nextstep.presentation.screen.main.home.state.FeedState
import com.tbacademy.nextstep.presentation.screen.main.home.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getFollowedPostsUseCase: GetFollowedPostsUseCase,
    private val createReactionUseCase: CreateReactionUseCase,
    private val updateReactionUseCase: UpdateReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase,
    private val createFollowUseCase: CreateGoalFollowUseCase,
    private val deleteFollowUseCase: DeleteGoalFollowUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect, Unit>(
    initialState = HomeState(),
    initialUiState = Unit
) {
    private val debounceJobs = mutableMapOf<String, Job>()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchPosts -> fetchPosts(feedState = state.value.feedState)
            is HomeEvent.HandleReactToPost -> reactToPost(
                id = event.postId,
                newReaction = event.reactionType
            )

            is HomeEvent.ToggleReactionsSelector -> toggleReactionSelector(
                id = event.postId,
                visible = event.visible
            )

            is HomeEvent.ToggleFollowGoal -> toggleFollowPost(postId = event.postId)

            is HomeEvent.OpenPostComments -> sendOpenPostCommentsEffect(
                postId = event.postId,
                typeActive = event.typeActive
            )

            is HomeEvent.UserSelected -> onUserSelected(userId = event.userId)
            is HomeEvent.FeedStateSelected -> handleFeedStateChanged(event.feedState)
            is HomeEvent.StartSearch -> onStartSearch()
            is HomeEvent.OpenMilestone -> onMilestoneSelected(goalId = event.goalId)
        }
    }

    private fun sendOpenPostCommentsEffect(postId: String, typeActive: Boolean) {
        viewModelScope.launch {
            emitEffect(HomeEffect.OpenComments(postId = postId, typeActive = typeActive))
        }
    }

    private fun handleFeedStateChanged(feedState: FeedState) {
        if (state.value.feedState != feedState) {
            updateState { this.copy(feedState = feedState) }
            fetchPosts(feedState = feedState)
        }
    }

    private fun fetchPosts(feedState: FeedState) {
        when (feedState) {
            FeedState.GLOBAL -> getGlobalPosts()
            FeedState.FOLLOWED -> getFollowedPosts()
        }
    }

    private fun onUserSelected(userId: String) {
        viewModelScope.launch {
            emitEffect(HomeEffect.NavigateToUserProfile(userId = userId))
        }
    }

    private fun onMilestoneSelected(goalId: String) {
        viewModelScope.launch {
            emitEffect(HomeEffect.NavigateToMilestone(goalId = goalId))
        }
    }

    private fun onStartSearch() {
        viewModelScope.launch {
            emitEffect(HomeEffect.NavigateToUserSearch)
        }
    }

    private fun reactToPost(id: String, newReaction: ReactionTypePresentation?) {
        val updatedPosts = state.value.posts?.map { post ->
            if (post.id != id) return@map post
            val oldReaction = post.userReaction

            val isAdding = oldReaction == null && newReaction != null
            val isRemoving = oldReaction != null && newReaction == null

            val newTotalCount = when {
                isAdding -> {
                    newReaction?.let {
                        debounceCreateReaction(postId = id, reactionType = it)
                    }
                    post.reactionCount + 1
                }

                isRemoving -> {
                    debounceDeleteReaction(postId = id)
                    post.reactionCount - 1
                }

                else -> {
                    newReaction?.let {
                        if (newReaction != oldReaction) {
                            debounceUpdateReaction(postId = id, reactionType = it)
                        }
                    }
                    post.reactionCount
                }
            }

            post.copy(
                userReaction = newReaction,
                reactionCount = newTotalCount,
                reactionFireCount = post.reactionFireCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = ReactionTypePresentation.FIRE
                ),
                reactionHeartCount = post.reactionHeartCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = ReactionTypePresentation.HEART
                ),
                reactionCookieCount = post.reactionCookieCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = ReactionTypePresentation.COOKIE
                ),
                reactionCheerCount = post.reactionCheerCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = ReactionTypePresentation.CHEER
                ),
                reactionDisappointmentCount = post.reactionDisappointmentCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = ReactionTypePresentation.DISAPPOINTMENT
                ),
                isReactionsPopUpVisible = false
            )
        } ?: return

        updateState { copy(posts = updatedPosts) }
    }

    private fun toggleReactionSelector(id: String, visible: Boolean) {
        state.value.posts?.let { posts ->
            val updatedPosts = posts.map { post ->
                if (post.id == id) {
                    post.copy(isReactionsPopUpVisible = visible)
                } else post
            }
            updateState { copy(posts = updatedPosts) }
        }
    }

    private fun toggleFollowPost(postId: String) {
        val currentPosts = state.value.posts ?: return
        val updatedPosts = currentPosts.map { post ->
            if (post.id == postId) {
                val newFollowState = if (!post.isUserFollowing) {
                    createFollow(followedId = post.goalId)
                    true
                } else {
                    deleteFollow(followedId = post.goalId)
                    false
                }
                post.copy(isUserFollowing = newFollowState)
            } else {
                post
            }
        }

        updateState { copy(posts = updatedPosts) }
    }

    private fun debounceCreateReaction(postId: String, reactionType: ReactionTypePresentation) {
        debounceJobs[postId]?.cancel()
        debounceJobs[postId] = viewModelScope.launch {
            delay(150)
            createReaction(postId = postId, reactionType = reactionType)
            debounceJobs.remove(postId)
        }
    }

    private fun debounceUpdateReaction(postId: String, reactionType: ReactionTypePresentation) {
        debounceJobs[postId]?.cancel()
        debounceJobs[postId] = viewModelScope.launch {
            delay(150)
            updateReaction(postId = postId, reactionType = reactionType)
            debounceJobs.remove(postId)
        }
    }

    private fun debounceDeleteReaction(postId: String) {
        debounceJobs[postId]?.cancel()
        debounceJobs[postId] = viewModelScope.launch {
            delay(150)
            deleteReaction(postId = postId)
            debounceJobs.remove(postId)
        }
    }

//    Api Calls

    private fun getGlobalPosts() {
        viewModelScope.launch {
            getPostsUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }
                    is Resource.Success -> updateState {
                        this.copy(posts = resource.data.map { it.toPresentation() }) }
                    is Resource.Error -> updateState { this.copy(error = resource.error) }
                }
            }
        }
    }

    private fun getFollowedPosts() {
        viewModelScope.launch {
            getFollowedPostsUseCase().collectLatest { resource ->
                when (resource) {
                    is Resource.Loading -> updateState { this.copy(isLoading = resource.loading) }
                    is Resource.Success -> updateState {
                        this.copy(posts = resource.data.map { it.toPresentation() })
                    }
                    is Resource.Error -> updateState { this.copy(error = resource.error) }
                }
            }
        }
    }

    private fun createReaction(postId: String, reactionType: ReactionTypePresentation) {
        viewModelScope.launch {
            createReactionUseCase(
                postId = postId,
                reactionType = reactionType.toDomain()
            ).collectLatest { resource ->
                resource.onError { error ->
                    emitEffect(effect = HomeEffect.ShowError(errorRes = error.toMessageRes()))
                }
            }
        }
    }

    private fun updateReaction(postId: String, reactionType: ReactionTypePresentation) {
        viewModelScope.launch {
            updateReactionUseCase(
                postId = postId, reactionType = reactionType.toDomain()
            ).collectLatest { resource ->
                resource.onError { error ->
                    emitEffect(effect = HomeEffect.ShowError(errorRes = error.toMessageRes()))
                }
            }
        }
    }

    private fun deleteReaction(postId: String) {
        viewModelScope.launch {
            deleteReactionUseCase(postId = postId).collectLatest { resource ->
                resource.onError { error ->
                    emitEffect(effect = HomeEffect.ShowError(errorRes = error.toMessageRes()))
                }
            }
        }
    }

    private fun createFollow(followedId: String) {
        viewModelScope.launch {
            createFollowUseCase(
                followedId = followedId,
            ).collectLatest { resource ->
                Log.d("FOLLOW_TEST_CREATE", "$resource")
            }
        }
    }

    private fun deleteFollow(followedId: String) {
        viewModelScope.launch {
            deleteFollowUseCase(
                followedId = followedId,
            ).collectLatest { resource ->
                Log.d("FOLLOW_TEST_DELETE", "$resource")
            }
        }
    }

}