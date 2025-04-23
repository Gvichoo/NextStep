package com.tbacademy.nextstep.presentation.screen.main.milestone

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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
    private val adapter by lazy {
        MilestonesAdapter(
            btnMarkAsDoneClicked = { milestoneId ->
                milestoneViewModel.onEvent(MilestoneEvent.MarkMilestoneAsDone(milestoneId))
            },

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
                Log.d("FragmentState", "$state")
                adapter.submitList(state.milestoneList)
                milestoneLoader.loaderContainer.isVisible = state.isLoading
            }
        }
    }

    override fun listeners() {}

    override fun observers() {}



}