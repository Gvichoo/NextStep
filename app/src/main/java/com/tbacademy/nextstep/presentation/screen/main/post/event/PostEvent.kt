package com.tbacademy.nextstep.presentation.screen.main.post.event

sealed interface PostEvent {
    data class GetPost(val postId: String): PostEvent
}