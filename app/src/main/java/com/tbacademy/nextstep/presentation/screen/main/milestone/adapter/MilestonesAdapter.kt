package com.tbacademy.nextstep.presentation.screen.main.milestone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.ItemMilestoneBinding
import com.tbacademy.nextstep.presentation.model.MilestonePresentation
import java.text.SimpleDateFormat
import java.util.*


class MilestonesAdapter(
    private val btnMarkAsDoneClicked: (milestoneId: Int) -> Unit,
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

                tvMilestoneTime.text = milestone.achievedAt?.let {
                    SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it.time))
                } ?: "Not Achieved"


                tvMilestoneTime.visibility = if (milestone.achieved) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                val colorRes = if (milestone.achieved) {
                    R.color.md_theme_tertiary
                } else {
                    R.color.md_theme_error
                }

                tvMilestoneTime.setTextColor(ContextCompat.getColor(itemView.context, colorRes))

                btnMarkAsDone.setOnClickListener {
                    btnMarkAsDoneClicked(milestone.id)
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