package com.tbacademy.nextstep.presentation.screen.main.milestone

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.tbacademy.nextstep.databinding.FragmentMilestoneBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.screen.main.milestone.adapter.MilestonesAdapter
import com.tbacademy.nextstep.presentation.screen.main.milestone.event.MilestoneEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MilestoneFragment : BaseFragment<FragmentMilestoneBinding>(FragmentMilestoneBinding::inflate) {

    private val milestoneViewModel: MilestoneViewModel by viewModels()
    private val args: MilestoneFragmentArgs by navArgs()
    private var targetDate: Long? = null

    private val adapter by lazy {
        MilestonesAdapter(
            onMarkAsDoneClick = { milestone ->
                val goalId = args.goalId
                milestoneViewModel.onEvent(MilestoneEvent.MarkMilestoneAsDone(goalId, milestone.id))
            },
            targetDate = targetDate
        )
    }

    override fun start() {
        val goalId = args.goalId
        milestoneViewModel.onEvent(MilestoneEvent.LoadMilestones(goalId))
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MilestoneFragment.adapter
        }
        observeUiState()
    }

    private fun observeUiState() {
        collect(flow = milestoneViewModel.state) { state ->
            binding.apply {
                targetDate = state.targetDate
                adapter.submitList(state.milestoneList)
                milestoneLoader.loaderContainer.isVisible = state.isLoading
            }
        }
    }

    override fun listeners() {}

    override fun observers() {}



}