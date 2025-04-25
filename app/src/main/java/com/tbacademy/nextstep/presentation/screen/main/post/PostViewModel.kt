package com.tbacademy.nextstep.presentation.screen.main.post

import com.tbacademy.nextstep.presentation.base.BaseViewModel
import com.tbacademy.nextstep.presentation.screen.main.post.effect.PostEffect
import com.tbacademy.nextstep.presentation.screen.main.post.event.PostEvent
import com.tbacademy.nextstep.presentation.screen.main.post.state.PostState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor() : BaseViewModel<PostState, PostEvent, PostEffect, Unit>
    (initialState = PostState(), Unit) {
    override fun onEvent(event: PostEvent) {
        when (event) {
            is PostEvent.GetPost -> {}
        }
    }
}