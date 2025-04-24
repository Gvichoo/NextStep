package com.tbacademy.nextstep.presentation.screen.main.milestone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemMilestoneBinding
import com.tbacademy.nextstep.presentation.model.MilestonePresentation
import java.text.SimpleDateFormat
import java.util.Locale


class MilestonesAdapter(
    private val onMarkAsDoneClick: (MilestonePresentation) -> Unit
) : ListAdapter<MilestonePresentation, MilestonesAdapter.MilestoneViewHolder>(MilestoneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding = ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MilestoneViewHolder(private val binding: ItemMilestoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(milestone: MilestonePresentation) {
            binding.apply {
                tvMilestoneTitle.text = milestone.text
                tvNumber.text = "${milestone.id + 1})"

                tvAchievedAt.text = milestone.achievedAt?.let { timestamp ->
                    val date = timestamp.toDate()
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                } ?: ""



                tvAchievedAt.isVisible = milestone.achieved

                btnMarkAsDone.isVisible = !milestone.achieved

                btnMarkAsDone.setOnClickListener {
                    onMarkAsDoneClick(milestone)
                }
            }
        }
    }

    class MilestoneDiffCallback : DiffUtil.ItemCallback<MilestonePresentation>() {
        override fun areItemsTheSame(oldItem: MilestonePresentation, newItem: MilestonePresentation): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MilestonePresentation, newItem: MilestonePresentation): Boolean {
            return oldItem == newItem
        }
    }
}