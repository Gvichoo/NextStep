package com.tbacademy.nextstep.data.common.mapper

import com.tbacademy.nextstep.data.remote.dto.CommentDto
import com.tbacademy.nextstep.domain.model.Comment

fun CommentDto.toDomain(): Comment {
    return Comment(
        id = id,
        postId = postId,
        authorId = authorId,
        authorUsername = authorUsername,
        authorProfilePictureUrl = authorProfilePictureUrl,
        text = text,
        createdAt = createdAt.toDate()
    )
}