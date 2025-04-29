package com.tbacademy.nextstep.domain.extension

import com.tbacademy.nextstep.domain.model.Post

fun List<Post>.sortedByNewest(): List<Post> {
    return sortedByDescending { it.createdAt }
}

fun List<Post>.sortedByOldest(): List<Post> {
    return sortedBy { it.createdAt }
}