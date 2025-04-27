package com.tbacademy.nextstep.presentation.screen.main.milestone.adapter

import android.util.Log
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
    private val onMarkAsDoneClick: (MilestonePresentation) -> Unit,
    private val targetDate: Long?,
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
                Log.d(
                    "MilestonesAdapter",
                    "Binding milestone: ${milestone.text}, isAuthor: ${milestone.isAuthor}"
                )

                tvMilestoneTitle.text = milestone.text
                tvNumber.text = "${milestone.id + 1})"

                // Make sure the date is always visible
                tvAchievedAt.isVisible = true

                // Set the achieved date or an empty string if not achieved
                tvAchievedAt.text = milestone.achievedAt?.let { timestamp ->
                    val date = timestamp.toDate()
                    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(date)
                } ?: "Not Achieved Yet" // Placeholder text when not achieved

                // Check visibility conditions for "Mark as Done" button
                btnMarkAsDone.isVisible = milestone.isAuthor && !milestone.achieved
                btnPost.isVisible = milestone.achieved && milestone.isAuthor

                val now = System.currentTimeMillis()
                val targetMillis = targetDate

                if (milestone.achieved) {
                    binding.status.text = "Done"
                    binding.status.isVisible = true
                } else if (targetMillis != null && now > targetMillis) {
                    binding.status.text = "Failed"
                    binding.status.isVisible = true
                    btnMarkAsDone.isVisible = false  // Hide the button if the milestone has failed
                } else {
                    binding.status.isVisible = false
                }


                btnMarkAsDone.setOnClickListener {
                    btnMarkAsDone.isVisible = false
                    onMarkAsDoneClick(milestone)
                }

                btnPost.setOnClickListener {
                    onPostClick(milestone.id.toString(), milestone.text)
                }
            }
        }
    }


    class MilestoneDiffCallback : DiffUtil.ItemCallback<MilestonePresentation>() {
        override fun areItemsTheSame(
            oldItem: MilestonePresentation,
            newItem: MilestonePresentation
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: MilestonePresentation,
            newItem: MilestonePresentation
        ): Boolean {
            return oldItem == newItem
        }
    }
}