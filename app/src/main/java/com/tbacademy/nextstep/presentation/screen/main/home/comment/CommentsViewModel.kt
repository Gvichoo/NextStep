package com.tbacademy.nextstep.presentation.screen.main.home.comment

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tbacademy.core.ApiError
import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.model.Comment
import com.tbacademy.nextstep.domain.usecase.comment.CreateCommentUseCase
import com.tbacademy.nextstep.domain.usecase.comment.GetCommentsUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.home.comment.effect.CommentsEffect
import com.tbacademy.nextstep.presentation.screen.main.home.comment.event.CommentsEvent
import com.tbacademy.nextstep.presentation.screen.main.home.comment.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.comment.model.CommentPresentation
import com.tbacademy.nextstep.presentation.screen.main.home.comment.state.CommentsState
import com.tbacademy.nextstep.presentation.screen.main.home.comment.state.CommentsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val createCommentUseCase: CreateCommentUseCase,
    private val getCommentsUseCase: GetCommentsUseCase
) :
    BaseViewModel<CommentsState, CommentsEvent, CommentsEffect, CommentsUiState>(
        initialState = CommentsState(),
        initialUiState = CommentsUiState()
    ) {
    override fun onEvent(event: CommentsEvent) {
        when (event) {
            is CommentsEvent.CommentChanged -> onCommentChanged(comment = event.comment)
            is CommentsEvent.UpdatePostId -> setPostId(postId = event.postId)
            is CommentsEvent.StartTyping -> sendStartTypingEffect()
            is CommentsEvent.CreateComment -> createComment()
        }
    }

    private fun setPostId(postId: String) {
        viewModelScope.launch {
            updateState { this.copy(postId = postId) }
            getComments(postId = state.value.postId)
        }
    }

    private fun sendStartTypingEffect() {
        viewModelScope.launch {
            Log.d("SHOULD_START_TYPING", "TRUE_VIEW")
            emitEffect(effect = CommentsEffect.StartTyping)
        }
    }

    private fun onCommentChanged(comment: String) {
        updateUiState { this.copy(comment = comment) }
    }

    private fun handleErrorReceived(error: ApiError) {
        viewModelScope.launch {
            emitEffect(effect = CommentsEffect.ShowError(error = error))
            updateState { this.copy(error = error) }
        }
    }

    private fun handleCommentCreated(comment: CommentPresentation) {
        viewModelScope.launch {
            updateState {
                this.copy(
                    comments = listOf(comment) + (comments
                        ?: emptyList())
                )
            }
            emitEffect(effect = CommentsEffect.CommentCreated)
        }
    }

    // Api Calls

    private fun getComments(postId: String) {
        viewModelScope.launch {
            getCommentsUseCase(postId = postId).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        updateState { this.copy(comments = resource.data.map { it.toPresentation() }) }
                    }
                }
            }
        }
    }

    private fun createComment() {
        viewModelScope.launch {
            createCommentUseCase(
                postId = state.value.postId,
                text = uiState.value.comment
            ).collectLatest { resource ->
                when (resource) {
                    is Resource.Error -> handleErrorReceived(error = resource.error)
                    is Resource.Success -> handleCommentCreated(comment = resource.data.toPresentation())
                    is Resource.Loading -> updateState { this.copy(uploadLoading = resource.loading) }
                }
            }
        }
    }

}