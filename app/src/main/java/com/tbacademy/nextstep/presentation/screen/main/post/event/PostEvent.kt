package com.tbacademy.nextstep.presentation.screen.main.post.event

sealed interface PostEvent {
    data class GetPost(val postId: String, val openComments: Boolean = false): PostEvent
    data class CommentsRequested(val postId: String): PostEvent
    data object ReturnRequested: PostEvent
}