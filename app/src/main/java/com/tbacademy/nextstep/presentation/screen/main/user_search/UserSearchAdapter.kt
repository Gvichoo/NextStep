package com.tbacademy.nextstep.presentation.screen.main.user_search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemSearchUserBinding
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.extension.loadProfilePictureGlide
import com.tbacademy.nextstep.presentation.screen.main.user_search.model.UserPresentation

class UserDiffUtil: DiffUtil.ItemCallback<UserPresentation>() {
    override fun areItemsTheSame(oldItem: UserPresentation, newItem: UserPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserPresentation, newItem: UserPresentation): Boolean {
        return oldItem == newItem
    }
}

class UserSearchAdapter(
    private val onUserClicked: (userId: String) -> Unit
): ListAdapter<UserPresentation,  UserSearchAdapter.UserViewHolder>(UserDiffUtil()) {
    inner class UserViewHolder(private val binding: ItemSearchUserBinding):
            RecyclerView.ViewHolder(binding.root) {
                fun onBind(user: UserPresentation) {
                    binding.apply {
                        tvUsername.text = user.username
                        ivProfile.loadProfilePictureGlide(url = user.profilePictureUrl)

                        root.setOnClickListener {
                            onUserClicked(user.id)
                        }
                    }
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: ItemSearchUserBinding =
            ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(user = getItem(position))
    }
}