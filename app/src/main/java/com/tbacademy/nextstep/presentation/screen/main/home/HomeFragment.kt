package com.tbacademy.nextstep.presentation.screen.main.home

import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentHomeBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.home.adapter.PostsAdapter
import com.tbacademy.nextstep.presentation.screen.main.home.comment.CommentsSheetFragment
import com.tbacademy.nextstep.presentation.screen.main.home.effect.HomeEffect
import com.tbacademy.nextstep.presentation.screen.main.home.event.HomeEvent
import com.tbacademy.nextstep.presentation.screen.main.home.state.FeedState
import com.tbacademy.nextstep.presentation.screen.main.main_screen.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val postsAdapter: PostsAdapter by lazy {
        PostsAdapter(
            updateUserReaction = { postId, reactionType ->
                homeViewModel.onEvent(
                    event = HomeEvent.HandleReactToPost(
                        postId = postId,
                        reactionType = reactionType
                    )
                )
            },
            reactionBtnHold = { postId, visible ->
                homeViewModel.onEvent(
                    event = HomeEvent.ToggleReactionsSelector(
                        postId = postId,
                        visible = visible
                    )
                )
            },
            commentsClicked = { postId ->
                homeViewModel.onEvent(event = HomeEvent.OpenPostComments(postId = postId))
            },
            commentsIconClicked = { postId ->
                homeViewModel.onEvent(
                    event = HomeEvent.OpenPostComments(
                        postId = postId,
                        typeActive = true
                    )
                )
            },
            followClicked = { postId ->
                homeViewModel.onEvent(HomeEvent.ToggleFollowGoal(postId = postId))
            },
            userClicked = { userId ->
                homeViewModel.onEvent(
                    event = HomeEvent.UserSelected(
                        userId = userId
                    )
                )
            },
            onMilestoneClicked = { goalId ->
                homeViewModel.onEvent(
                    event = HomeEvent.OpenMilestone(
                        goalId = goalId
                    )
                )
            }
        )
    }

    private val homeViewModel: HomeViewModel by viewModels()

    override fun start() {
        homeViewModel.onEvent(HomeEvent.FetchPosts)
        setPostsAdapter()
    }

    override fun listeners() {
        feedTypeToggleListener()
        searchBtnListener()
    }

    override fun observers() {
        observeState()
        observeEffects()
    }

    private fun observeState() {
        collectLatest(flow = homeViewModel.state) { state ->
                postsAdapter.submitList(state.posts)
            binding.pbPosts.isVisible = state.isLoading && state.posts.isNullOrEmpty()

            Log.d("HOME_STATE", "$state")
        }
    }

    private fun observeEffects() {
        collect(flow = homeViewModel.effects) { effect ->
            when (effect) {
                is HomeEffect.ShowError -> effect.errorRes?.let { showMessage(message = it) }
                is HomeEffect.OpenComments -> openCommentsBottomSheet(
                    postId = effect.postId,
                    typeActive = effect.typeActive
                )

                is HomeEffect.NavigateToUserProfile -> navigateToProfile(userId = effect.userId)
                is HomeEffect.NavigateToUserSearch -> navigateToSearch()
                is HomeEffect.NavigateToMilestone -> navigateToMilestone(goalId = effect.goalId)
            }
        }
    }

    private fun feedTypeToggleListener() {
        binding.postTypeToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            when (checkedId) {
                R.id.btnGlobal -> {
                    homeViewModel.onEvent(HomeEvent.FeedStateSelected(FeedState.GLOBAL))
                }

                R.id.btnFollowed -> {
                    homeViewModel.onEvent(HomeEvent.FeedStateSelected(FeedState.FOLLOWED))
                }
            }
        }
    }


    private fun searchBtnListener() {
        binding.btnSearch.setOnClickListener {
            homeViewModel.onEvent(HomeEvent.StartSearch)
        }
    }

    private fun openCommentsBottomSheet(postId: String, typeActive: Boolean) {
        val commentsSheet = CommentsSheetFragment()
        commentsSheet.arguments = Bundle().apply {
            putString("postId", postId)
            putBoolean("typeActive", typeActive)
        }
        commentsSheet.show(childFragmentManager, "CommentsSheet")
    }

    private fun navigateToProfile(userId: String?) {
        val action = MainFragmentDirections.actionMainFragmentToProfileFragment(
            userId = userId
        )
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun navigateToMilestone(goalId : String){
        val action = MainFragmentDirections.actionMainFragmentToMilestoneFragment(
            goalId = goalId
        )
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun navigateToSearch() {
        val action = MainFragmentDirections.actionMainFragmentToUserSearchFragment()
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun setPostsAdapter() {
        binding.rvPosts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPosts.adapter = postsAdapter
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}