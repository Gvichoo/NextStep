package com.tbacademy.nextstep.presentation.screen.main.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemProfileGoalBinding
import com.tbacademy.nextstep.presentation.extension.loadImagesGlide
import com.tbacademy.nextstep.presentation.extension.loadProfilePictureGlide
import com.tbacademy.nextstep.presentation.screen.main.profile.model.GoalPresentation


class GoalDiffUtil : DiffUtil.ItemCallback<GoalPresentation>() {
    override fun areItemsTheSame(oldItem: GoalPresentation, newItem: GoalPresentation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GoalPresentation, newItem: GoalPresentation): Boolean {
        return oldItem == newItem
    }
}

class GoalAdapter : ListAdapter<GoalPresentation, GoalAdapter.GoalViewHolder>(GoalDiffUtil()) {
    inner class GoalViewHolder(private val binding: ItemProfileGoalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(goal: GoalPresentation) {
            binding.apply {
                tvGoalTitle.text = goal.title
                tvGoalStatus.text = goal.goalStatus.toString()
                ivGoal.loadProfilePictureGlide(url = goal.imageUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val binding: ItemProfileGoalBinding =
            ItemProfileGoalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GoalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.onBind(goal = getItem(position))
    }
}