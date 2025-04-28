package com.tbacademy.nextstep.presentation.screen.main.add.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemOfMilestonesBinding
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.model.MilestoneItem


class MilestoneAdapter(
    private val onTextChanged: (position: Int, newText: String) -> Unit
): ListAdapter<MilestoneItem, MilestoneAdapter.MilestoneViewHolder>(MilestoneDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding = ItemOfMilestonesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        val milestoneItem = getItem(position)
        holder.bind(milestoneItem)
    }

    inner class MilestoneViewHolder(private val binding: ItemOfMilestonesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(milestone: MilestoneItem) {
            binding.etMilestone.apply {
                if (text.toString() != milestone.text) {
                    setText(milestone.text)
                    setSelection(milestone.text.length)
                }

                onTextChanged { newText ->
                    milestone.text = newText  // update item locally
                    // No need to call submitList() here
                    if (milestone.errorMessage != null) {
                        binding.tlMilestone.error = null
                    }
                }
            }

            binding.tlMilestone.apply {
                error = milestone.errorMessage?.let { context.getString(it) }
            }
        }
    }

    class MilestoneDiffCallback : DiffUtil.ItemCallback<MilestoneItem>() {
        override fun areItemsTheSame(oldItem: MilestoneItem, newItem: MilestoneItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MilestoneItem, newItem: MilestoneItem): Boolean {
            return oldItem == newItem
        }
    }

}