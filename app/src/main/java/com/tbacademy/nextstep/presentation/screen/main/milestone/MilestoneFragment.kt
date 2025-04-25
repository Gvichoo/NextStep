package com.tbacademy.nextstep.presentation.screen.main.milestone

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentMilestoneBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.screen.main.milestone.adapter.MilestonesAdapter
import com.tbacademy.nextstep.presentation.screen.main.milestone.effect.MilestoneEffect
import com.tbacademy.nextstep.presentation.screen.main.milestone.event.MilestoneEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MilestoneFragment :
    BaseFragment<FragmentMilestoneBinding>(FragmentMilestoneBinding::inflate) {

    private val milestoneViewModel: MilestoneViewModel by viewModels()
    private val args: MilestoneFragmentArgs by navArgs()
    private var targetDate: Long? = null

    private val adapter by lazy {
        MilestonesAdapter(
            onMarkAsDoneClick = { milestone ->
                val goalId = args.goalId
                milestoneViewModel.onEvent(MilestoneEvent.MarkMilestoneAsDone(goalId, milestone.id))
            },
            targetDate = targetDate,
            onPostClick = { milestoneId, text,goalId ->
                milestoneViewModel.onEvent(
                    event = MilestoneEvent.OpenMilestone(
                        milestoneId = milestoneId,
                        text = text,
                        goalId = goalId
                    )
                )
            }
        )
    }

    override fun start() {
        val goalId = args.goalId
        milestoneViewModel.onEvent(MilestoneEvent.LoadMilestones(goalId))
        setUpRecycler()
    }


    override fun listeners() {}

    override fun observers() {
        observeEffects()
        observeUiState()
    }

    private fun navigateToMilestone(milestoneId: String, text: String,goalId : String) {
        val action = MilestoneFragmentDirections.actionMilestoneFragmentToPostMilestoneFragment(
            milestoneId = milestoneId,
            text = text,
            goalId = goalId
        )
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun setUpRecycler() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@MilestoneFragment.adapter
        }
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

    private fun observeEffects() {
        collect(flow = milestoneViewModel.effects) { effects ->
            when (effects) {
                MilestoneEffect.NavigateBack -> {}
                is MilestoneEffect.NavigateToMilestonePost -> navigateToMilestone(
                    milestoneId = effects.milestoneId,
                    text = effects.text,
                    goalId = effects.goalId
                )

                is MilestoneEffect.ShowError -> {}
            }
        }
    }
}