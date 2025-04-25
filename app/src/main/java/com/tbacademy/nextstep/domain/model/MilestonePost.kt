package com.tbacademy.nextstep.domain.model

import android.net.Uri
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation
import java.util.Date

data class MilestonePost(
    val title: String,
    val description: String,
    val imageUri: Uri? = null,
    val goalId: String = "",
)


data class MilestonePostDto(
    val title: String,
    val description: String,
    val imageUrl: String,
    val authorId: String,
    val authorUsername: String,
    val id: String,
    val goalTitle: String = ""

)


fun MilestonePostDto.toPost(): Post {
    return Post(
        id = this.id,
        title = this.title,
        description = this.description,
        imageUrl = this.imageUrl,
        authorId = this.authorId,
        authorUsername = this.authorUsername,
        commentCount = 0,
        reactionCount = 0,
        reactionFireCount = 0,
        reactionHeartCount = 0,
        reactionCookieCount = 0,
        reactionCheerCount = 0,
        reactionDisappointedCount = 0,
        userReaction = null,
        isUserFollowing = false,
        isOwnPost = false,
        goalId = "",
        createdAt = Date(),

    )
}

//
//fun MilestonePost.toPostPresentation(authorUsername: String, isOwnPost: Boolean): PostPresentation {
//    return PostPresentation(
//        id = "",  // Set this appropriately
//        authorId = "", // Set this as well
//        authorUsername = authorUsername,
//        goalId = this.goalId,
//        title = this.title,
//        description = this.description,
//        reactionCount = 0, // Set initial value for reactions
//        commentCount = 0,  // Set initial value for comments
//        imageUrl = this.imageUri?.toString(),
//        createdAt = Date().toString(),  // Set the current time for createdAt
//        isOwnPost = isOwnPost,
//        isReactionsPopUpVisible = false
//    )
//}



fun MilestonePost.toDto(): MilestonePostDto {
    return MilestonePostDto(
        title = this.title,
        description = this.description,
        imageUrl = this.imageUri?.toString() ?: "", // Convert Uri to String or default to empty string
        authorId = "", // Placeholder, to be set later
        authorUsername = "", // Placeholder, to be set later
        id = ""  // Firestore ID will be generated later
    )
}

