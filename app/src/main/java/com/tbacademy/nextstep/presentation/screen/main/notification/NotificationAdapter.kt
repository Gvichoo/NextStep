package com.tbacademy.nextstep.presentation.screen.main.notification

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemNotificationBinding
import com.tbacademy.nextstep.presentation.extension.loadProfilePictureGlide
import com.tbacademy.nextstep.presentation.screen.main.notification.model.NotificationPresentation

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

class NotificationAdapter :
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
                ivReactionIcon.isVisible = notification.reactionType != null
                notification.reactionType?.let {
                    ivReactionIcon.setImageResource(it.iconRes)
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