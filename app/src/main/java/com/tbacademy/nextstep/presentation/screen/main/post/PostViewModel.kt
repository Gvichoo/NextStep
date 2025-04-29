package com.tbacademy.nextstep.presentation.screen.main.post

import androidx.lifecycle.viewModelScope
import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.usecase.post.GetPostUseCase
import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.common.mapper.toMessageRes
import com.tbacademy.nextstep.presentation.screen.main.home.mapper.toPresentation
import com.tbacademy.nextstep.presentation.screen.main.post.effect.PostEffect
import com.tbacademy.nextstep.presentation.screen.main.post.event.PostEvent
import com.tbacademy.nextstep.presentation.screen.main.post.state.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase
) : BaseViewModel<PostState, PostEvent, PostEffect>(
    initialState = PostState()
) {

    override fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.GetPost -> getPost(
                postId = event.postId,
                showComments = event.openComments
            )

            is PostEvent.CommentsRequested -> onCommentsRequest(postId = event.postId)
            is PostEvent.ReturnRequested -> onReturnRequested()
        }
    }

    private fun onCommentsRequest(postId: String) {
        viewModelScope.launch {
            emitEffect(effect = PostEffect.OpenCommentsBottomSheet(postId = postId))
        }
    }

    private fun onReturnRequested() {
        viewModelScope.launch {
            emitEffect(effect = PostEffect.NavigateBack)
        }
    }

    private fun getPost(postId: String, showComments: Boolean) {
        viewModelScope.launch {
            getPostUseCase(postId = postId).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        updateState {
                            copy(post = resource.data.toPresentation())
                        }

                        if (showComments)
                            emitEffect(effect = PostEffect.OpenCommentsBottomSheet(postId = postId))
                    }

                    is Resource.Error -> {
                        updateState { copy(errorMessageRes = resource.error.toMessageRes()) }
                        emitEffect(effect = PostEffect.ShowErrorMessage(errorMessageRes = resource.error.toMessageRes()))
                    }

                    is Resource.Loading -> updateState { copy(isLoading = resource.loading) }
                }
            }
        }
    }
}