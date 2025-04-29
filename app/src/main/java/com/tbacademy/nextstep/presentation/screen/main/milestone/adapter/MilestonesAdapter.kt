package com.tbacademy.nextstep.presentation.screen.main.milestone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.ItemMilestoneBinding
import com.tbacademy.nextstep.presentation.model.MilestonePresentation
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.firebase.Timestamp
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class MilestonesAdapter(
    private val onMarkAsDoneClick: (MilestonePresentation) -> Unit,
    private val onPostClick: (milestoneId: String, text: String) -> Unit
) : ListAdapter<MilestonePresentation, MilestonesAdapter.MilestoneViewHolder>(MilestoneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding =
            ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                tvNumber.text = "${milestone.id + 1}"

                tvAchievedAt.isVisible = true
                tvAchievedAt.text = milestone.achievedAt?.let { timestamp ->
                    val date = timestamp.toDate()
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                } ?: "Not Achieved Yet"

                // Convert targetDate to milliseconds
                val targetMillis = milestone.targetDate?.toDate()?.time


                val now = Calendar.getInstance(TimeZone.getTimeZone("GMT+4")).timeInMillis

                // If current time is greater than target date, mark as "failed"
                if (milestone.achieved) {
                    chipStatus.text = itemView.context.getString(R.string.done)
                    chipStatus.isVisible = true
                    chipStatus.setChipBackgroundColorResource(R.color.md_theme_tertiaryContainer)
                    chipStatus.setTextColor(itemView.context.getColor(R.color.md_theme_onTertiaryContainer))

                    tvAchievedAt.text = milestone.achievedAt?.let { timestamp ->
                        val date = timestamp.toDate()
                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                    } ?: "Not Achieved Yet"

                    btnMarkAsDone.isVisible = false
                    btnPost.isVisible = milestone.isAuthor
                } else if (targetMillis != null && now >= targetMillis) {
                    chipStatus.text = itemView.context.getString(R.string.failed)
                    chipStatus.isVisible = true
                    chipStatus.setChipBackgroundColorResource(R.color.md_theme_errorContainer)
                    chipStatus.setTextColor(itemView.context.getColor(R.color.md_theme_onErrorContainer))
                    tvAchievedAt.text = itemView.context.getString(R.string.faileddd)

                    btnMarkAsDone.isVisible = false
                    btnPost.isVisible = false
                } else {
                    chipStatus.isVisible = false
                    tvAchievedAt.text = itemView.context.getString(R.string.not_achieved_yet)
                    btnMarkAsDone.isVisible = milestone.isAuthor
                    btnPost.isVisible = false
                }

                btnMarkAsDone.setOnClickListener {
                    val updatedMilestone = milestone.copy(
                        achieved = true,
                        achievedAt = Timestamp(Date(System.currentTimeMillis()))  // Wrap currentTimeMillis() in Date
                    )
                    onMarkAsDoneClick(updatedMilestone)
                }

                btnPost.setOnClickListener {
                    onPostClick(milestone.id.toString(), milestone.text)
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