package com.tbacademy.nextstep.presentation.screen.main.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemNotificationBinding
import com.tbacademy.nextstep.domain.model.Notification
import com.tbacademy.nextstep.presentation.extension.loadProfilePictureGlide
import com.tbacademy.nextstep.presentation.screen.main.notification.model.NotificationPresentation
import com.tbacademy.nextstep.presentation.screen.main.notification.model.NotificationTypePresentation

class NotificationDiffUtil : DiffUtil.ItemCallback<NotificationPresentation>() {
    override fun areItemsTheSame(
        oldItem: NotificationPresentation,
        newItem: NotificationPresentation
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: NotificationPresentation,
        newItem: NotificationPresentation
    ): Boolean {
        return oldItem == newItem
    }
}

class NotificationAdapter(
    private val reactionNotificationClicked: (postId: String, isComment: Boolean) -> Unit,
    private val followNotificationClicked: (userId: String) -> Unit
) :
    ListAdapter<NotificationPresentation, NotificationAdapter.NotificationViewHolder>(
        NotificationDiffUtil()
    ) {
    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(notification: NotificationPresentation) {
            binding.apply {
                tvAuthor.text = notification.authorUsername
                ivProfile.loadProfilePictureGlide(url = notification.authorProfilePictureUrl)
                tvDate.text = notification.createdAt
                tvAction.text = itemView.context.getString(notification.type.messageRes)

                // Post Reacted
                if (notification.type == NotificationTypePresentation.POST_REACTED) {
                    root.setOnClickListener {
                        notification.postId?.let { id -> reactionNotificationClicked(id, false) }
                    }

                    notification.reactionType?.let {
                        ivNotificationIcon.setImageResource(it.iconRes)
                        ivNotificationIcon.setBackgroundResource(it.backgroundRes)
                    }
                }

                // Post Commented
                if (notification.type == NotificationTypePresentation.POST_COMMENTED) {
                    notification.type.iconRes?.let { ivNotificationIcon.setImageResource(it) }
                    notification.type.backgroundRes?.let { ivNotificationIcon.setBackgroundResource(it)
                    }
                    root.setOnClickListener {
                        notification.postId?.let { id -> reactionNotificationClicked(id, true) }
                    }
                }

                // Post Followed
                if (notification.type == NotificationTypePresentation.USER_FOLLOWED) {
                    notification.type.iconRes?.let { ivNotificationIcon.setImageResource(it) }
                    notification.type.backgroundRes?.let { ivNotificationIcon.setBackgroundResource(it)
                    }
                    root.setOnClickListener {
                        followNotificationClicked(notification.fromId)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding =
            ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.onBind(notification = getItem(position))
    }
}