package com.tbacademy.nextstep.presentation.screen.main.goal

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbacademy.nextstep.databinding.FragmentGoalBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.goal.effect.GoalEffect
import com.tbacademy.nextstep.presentation.screen.main.goal.event.GoalEvent
import com.tbacademy.nextstep.presentation.screen.main.home.comment.CommentsSheetFragment
import com.tbacademy.nextstep.presentation.screen.main.home.model.PostType
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

        setAdapter()
    }

    override fun listeners() {
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

                state.posts?.let {
                    tvGoalTitle.text =
                        state.posts.firstOrNull { it.type == PostType.GOAL }?.title
                }
            }
        }
    }

    private fun observeEffect() {
        collect(flow = goalViewModel.effects) { effect ->
            when(effect) {
                is GoalEffect.OpenComments -> openCommentsBottomSheet(postId = effect.postId)
            }
        }
    }

    private fun setAdapter() {
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = goalPostAdapter
    }

    private fun getPosts(goalId: String) {
        goalViewModel.onEvent(event = GoalEvent.FetchGoalPosts(goalId = goalId))
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