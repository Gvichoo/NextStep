package com.tbacademy.nextstep.presentation.screen.main.home

import androidx.lifecycle.viewModelScope
import com.tbacademy.nextstep.domain.core.Resource
import com.tbacademy.nextstep.domain.core.onError
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
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostReactionType
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
    private val createReactionUseCase: CreateReactionUseCase,
    private val updateReactionUseCase: UpdateReactionUseCase,
    private val deleteReactionUseCase: DeleteReactionUseCase
) : BaseViewModel<HomeState, HomeEvent, HomeEffect, Unit>(
    initialState = HomeState(),
    initialUiState = Unit
) {
    private val debounceJobs = mutableMapOf<String, Job>()

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.FetchGlobalPosts -> getGlobalPosts()
            is HomeEvent.HandleReactToPost -> reactToPost(
                id = event.postId,
                newReaction = event.reactionType
            )

            is HomeEvent.ToggleReactionsSelector -> toggleReactionSelector(
                id = event.postId,
                visible = event.visible
            )

            is HomeEvent.OpenPostComments -> sendOpenPostCommentsEffect(postId = event.postId, typeActive = event.typeActive)
            is HomeEvent.UserSelected -> onUserSelected(userId = event.userId)
        }
    }

    private fun sendOpenPostCommentsEffect(postId: String, typeActive: Boolean) {
        viewModelScope.launch {
            emitEffect(HomeEffect.OpenComments(postId = postId, typeActive = typeActive))
        }
    }

    private fun onUserSelected(userId: String) {
        viewModelScope.launch {
            emitEffect(HomeEffect.NavigateToUserProfile(userId = userId))
        }
    }

    private fun reactToPost(id: String, newReaction: PostReactionType?) {
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
                    target = PostReactionType.FIRE
                ),
                reactionHeartCount = post.reactionHeartCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.HEART
                ),
                reactionCookieCount = post.reactionCookieCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.COOKIE
                ),
                reactionCheerCount = post.reactionCheerCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.CHEER
                ),
                reactionDisappointmentCount = post.reactionDisappointmentCount.adjustCount(
                    old = oldReaction,
                    new = newReaction,
                    target = PostReactionType.DISAPPOINTMENT
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

    private fun debounceCreateReaction(postId: String, reactionType: PostReactionType) {
        debounceJobs[postId]?.cancel()
        debounceJobs[postId] = viewModelScope.launch {
            delay(150)
            createReaction(postId = postId, reactionType = reactionType)
            debounceJobs.remove(postId)
        }
    }

    private fun debounceUpdateReaction(postId: String, reactionType: PostReactionType) {
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
                    is Resource.Success -> updateState { this.copy(posts = resource.data.map { it.toPresentation() }) }
                    is Resource.Error -> updateState { this.copy(error = resource.error) }
                }
            }
        }
    }

    private fun createReaction(postId: String, reactionType: PostReactionType) {
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

    private fun updateReaction(postId: String, reactionType: PostReactionType) {
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

}