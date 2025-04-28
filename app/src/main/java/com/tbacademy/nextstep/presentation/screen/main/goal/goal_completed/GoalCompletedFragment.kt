package com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentGoalCompletedBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.effect.GoalCompletedEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.goal_completed.event.GoalCompletedEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GoalCompletedFragment :
    BaseFragment<FragmentGoalCompletedBinding>(FragmentGoalCompletedBinding::inflate) {

    private val goalCompletedViewModel: GoalCompletedViewModel by viewModels()

    override fun start() {
        goalCompletedViewModel.onEvent(event = GoalCompletedEvent.StartAnimation)
    }

    override fun listeners() {
        continueBtnListener()
    }

    override fun observers() {
        observeState()
        observeEvent()
    }

    private fun observeState() {
        collectLatest(flow = goalCompletedViewModel.state) { state ->
            if (state.messageVisible) {
                playCelebration()
            }
        }
    }

    private fun observeEvent() {
        collect(flow = goalCompletedViewModel.effects) { effect ->
            when (effect) {
                is GoalCompletedEffect.NavigateToHome -> navigateToHome()
            }
        }
    }

    private fun continueBtnListener() {
        binding.btnContinue.setOnClickListener {
            goalCompletedViewModel.onEvent(event = GoalCompletedEvent.Continue)
        }
    }

    private fun navigateToHome() {
        val action = GoalCompletedFragmentDirections.actionGoalCompletedFragmentToMainFragment()
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun playCelebration() {
        binding.lottieSuccess.visibility = View.VISIBLE
        binding.lottieSuccess.playAnimation()

        lifecycleScope.launch {
            delay(200)
            binding.tvGoalCompleted.animate()
                .alpha(1f)
                .setDuration(1000L)
                .start()
            delay(200)
            binding.btnContinue.animate()
                .alpha(1f)
                .setDuration(200L)
                .start()
        }
    }
}