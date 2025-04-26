package com.tbacademy.nextstep.presentation.screen.main.goal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.ItemGoalPostBinding
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.screen.main.home.extension.topReactions
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostPresentation

class PostDiffUtil: DiffUtil.ItemCallback<PostPresentation>() {
    override fun areItemsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostPresentation, newItem: PostPresentation): Boolean {
        return oldItem == newItem
    }
}

class GoalPostAdapter(
    private val commentsClicked: (postId: String) -> Unit
): ListAdapter<PostPresentation, GoalPostAdapter.PostViewHolder>(PostDiffUtil()) {
    inner class PostViewHolder(private val binding: ItemGoalPostBinding)
        :RecyclerView.ViewHolder(binding.root) {
            fun onBind(post: PostPresentation) {
                binding.apply {
                    tvTitle.text = post.title
                    tvDescription.text = post.description
                    tvDate.text = post.createdAt
                    post.imageUrl?.let { ivPostImage.loadImagesGlide(url = it) }
                    tvCommentsCount.text = post.commentCount.toString()
                    tvReactionsCount.text = post.reactionCount.toString()

                    tvComments.setOnClickListener {
                        commentsClicked(post.id)
                    }


                    // Post Reactions
                    val topReactions = post.topReactions()

                    // Top 3 Reactions
                    ivReaction1.setImageResource(
                        topReactions.getOrNull(0)?.first?.iconRes ?: R.drawable.ic_reaction_fire_24px
                    )
                    ivReaction1.setBackgroundResource(
                        topReactions.getOrNull(0)?.first?.backgroundRes
                            ?: R.drawable.bg_reaction_fire
                    )

                    ivReaction2.isVisible = topReactions.size > 1
                    ivReaction2.setImageResource(topReactions.getOrNull(1)?.first?.iconRes ?: 0)
                    ivReaction2.setBackgroundResource(topReactions.getOrNull(1)?.first?.backgroundRes ?: 0)

                    ivReaction3.isVisible = topReactions.size > 2
                    ivReaction3.setImageResource(topReactions.getOrNull(2)?.first?.iconRes ?: 0)
                    ivReaction3.setBackgroundResource(topReactions.getOrNull(2)?.first?.backgroundRes ?: 0)
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemGoalPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.onBind(post = getItem(position))
    }
}