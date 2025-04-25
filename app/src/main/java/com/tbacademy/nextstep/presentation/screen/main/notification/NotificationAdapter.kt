package com.tbacademy.nextstep.presentation.screen.main.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemNotificationBinding
import com.tbacademy.nextstep.domain.model.NotificationType
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
    private val reactionNotificationClicked: (postId: String) -> Unit
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
                ivNotificationIcon.isVisible = notification.reactionType != null
                notification.reactionType?.let {
                    ivNotificationIcon.setImageResource(it.iconRes)
                    ivNotificationIcon.setBackgroundResource(it.backgroundRes)
                }


                if (notification.type == NotificationTypePresentation.POST_REACTED) {
                    root.setOnClickListener {
                        notification.postId?.let { id -> reactionNotificationClicked(id) }
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