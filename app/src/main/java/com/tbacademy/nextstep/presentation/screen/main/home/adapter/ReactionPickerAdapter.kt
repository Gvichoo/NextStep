package com.tbacademy.nextstep.presentation.screen.main.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tbacademy.nextstep.databinding.ItemReactionOptionBinding
import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionTypePresentation
import com.tbacademy.nextstep.presentation.screen.main.home.model.ReactionOption

class ReactionPickerAdapter(
    private val reactions: List<ReactionOption>,
    private val onReactionSelected: (ReactionTypePresentation) -> Unit,
    private val onReactionHovered: ((ReactionTypePresentation?) -> Unit)? = null
) : RecyclerView.Adapter<ReactionPickerAdapter.ReactionViewHolder>() {

    inner class ReactionViewHolder(private val binding: ItemReactionOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ClickableViewAccessibility")
        fun onBind(option: ReactionOption) {
            binding.apply {
                ivReaction.setImageResource(option.type.iconRes)
                ivReaction.setBackgroundResource(option.type.backgroundRes)

                root.setOnTouchListener { _, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                            onReactionHovered?.invoke(option.type)
                            true
                        }

                        MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                            onReactionHovered?.invoke(null)
                            onReactionSelected(option.type)
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionViewHolder {
        val binding: ItemReactionOptionBinding =
            ItemReactionOptionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReactionViewHolder(binding)
    }

    override fun getItemCount(): Int = reactions.size

    override fun onBindViewHolder(holder: ReactionViewHolder, position: Int) {
        holder.onBind(option = reactions[position])
    }
}