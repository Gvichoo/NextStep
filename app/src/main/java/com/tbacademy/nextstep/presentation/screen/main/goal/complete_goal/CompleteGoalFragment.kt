package com.tbacademy.nextstep.presentation.screen.main.goal.complete_goal

import androidx.fragment.app.viewModels
import com.tbacademy.nextstep.databinding.FragmentCompleteGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompleteGoalFragment :
    BaseFragment<FragmentCompleteGoalBinding>(FragmentCompleteGoalBinding::inflate) {

    private val completeGoalViewModel: CompleteGoalViewModel by viewModels()

    override fun start() {

    }

    override fun listeners() {

    }

    override fun observers() {

    }
}