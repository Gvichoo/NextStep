package com.tbacademy.nextstep.presentation.screen.main.goal

import android.os.Bundle
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.goal.effect.GoalEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.event.GoalEvent
import com.tbacademy.nextstep.presentation.screen.main.home.comment.CommentsSheetFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalFragment : BaseFragment<FragmentGoalBinding>(FragmentGoalBinding::inflate) {

    private val goalPostAdapter by lazy {
        GoalPostAdapter(
            commentsClicked = { postId ->
                goalViewModel.onEvent(event = GoalEvent.CommentSelected(postId = postId))
            }
        )
    }
    private val goalViewModel: GoalViewModel by viewModels()
    private val args: GoalFragmentArgs by navArgs()

    override fun start() {
        getPosts(goalId = args.goalId)
        goalViewModel.onEvent(event = GoalEvent.CheckGoalAuthor(isOwnGoal = args.isOwnGoal))

        setAdapter()
    }

    override fun listeners() {
        setCompleteBtnListener()
    }

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun observeState() {
        collectLatest(flow = goalViewModel.state) { state ->
            goalPostAdapter.submitList(state.posts)

            binding.apply {
                pbPosts.isVisible = state.isLoading
                btnComplete.isVisible = state.isOwnGoal && args.goalActive
                tvGoalTitle.text = state.goalTitle
                if (state.isGoalCompleteBottomSheetVisible) {
                    showCompleteGoalBottomSheet()
                }
            }
        }
    }

    private fun observeEffect() {
        collect(flow = goalViewModel.effects) { effect ->
            when (effect) {
                is GoalEffect.OpenComments -> openCommentsBottomSheet(postId = effect.postId)
                is GoalEffect.NavigateToCompleteGoal -> navigateToCompleteGoal(goalTitle = effect.goalTitle)
            }
        }
    }

    private fun navigateToCompleteGoal(goalTitle: String) {
        val action = GoalFragmentDirections.actionGoalFragmentToCompleteGoalFragment(
            goalId = args.goalId,
            // ☣️☢️ To be looked AGAIN
            goalTitle = goalTitle
        )
        findNavController().navigate(action)
    }

    private fun setAdapter() {
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = goalPostAdapter
    }

    private fun getPosts(goalId: String) {
        goalViewModel.onEvent(event = GoalEvent.FetchGoalPosts(goalId = goalId))
    }

    private fun setCompleteBtnListener() {
        binding.btnComplete.setOnClickListener {
            goalViewModel.onEvent(event = GoalEvent.OpenCompleteGoalSheet)
        }
    }

    private var bottomSheetDialog: BottomSheetDialog? = null

    private fun showCompleteGoalBottomSheet() {
        if (bottomSheetDialog?.isShowing == true) {
            return
        }

        val parent = FrameLayout(requireContext())

        val view = layoutInflater.inflate(R.layout.dialog_proceed_complition, parent, false)

        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog?.setContentView(view)

        view.findViewById<TextView>(R.id.optionYes).setOnClickListener {
            goalViewModel.onEvent(event = GoalEvent.ProceedWithGoalCompletion)
            bottomSheetDialog?.dismiss()
        }

        view.findViewById<TextView>(R.id.optionNo).setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog?.setOnDismissListener {
            goalViewModel.onEvent(GoalEvent.CloseCompleteGoalSheet)
        }

        bottomSheetDialog?.show()
    }

    private fun openCommentsBottomSheet(postId: String) {
        val commentsSheet = CommentsSheetFragment()
        commentsSheet.arguments = Bundle().apply {
            putString("postId", postId)
            putBoolean("typeActive", false)
        }
        commentsSheet.show(childFragmentManager, "CommentsSheet")
    }

}