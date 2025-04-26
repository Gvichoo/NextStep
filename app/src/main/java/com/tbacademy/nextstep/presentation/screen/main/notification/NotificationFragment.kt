package com.tbacademy.nextstep.presentation.screen.main.notification

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentNotificationBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.showSnackbar
import com.tbacademy.nextstep.presentation.screen.main.main_screen.MainFragmentDirections
import com.tbacademy.nextstep.presentation.screen.main.notification.effect.NotificationEffect
import com.tbacademy.nextstep.presentation.screen.main.notification.event.NotificationEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val notificationViewModel: NotificationViewModel by viewModels()

    private val notificationAdapter by lazy {
        NotificationAdapter(
            reactionNotificationClicked = { postId, isComment ->
                onReactNotificationSelected(postId = postId, isComment = isComment)
            },
            followNotificationClicked = { userId ->
                notificationViewModel.onEvent(event = NotificationEvent.FollowNotificationSelected(userId = userId))
            }
        )
    }

    override fun start() {
        setUpNotificationAdapter()
        notificationViewModel.onEvent(event = NotificationEvent.GetNotifications())
    }

    override fun listeners() {
        setReactionsSwipeListener()
    }

    override fun observers() {
        observeState()
        observeEffect()
    }


    private fun observeState() {
        collectLatest(flow = notificationViewModel.state) { state ->
            binding.apply {
                pbNotifications.isVisible = state.isLoading && !state.isRefreshing
                swipeRefreshLayoutNotifications.isRefreshing = state.isRefreshing
            }

            notificationAdapter.submitList(state.notifications) {
                binding.rvNotifications.scrollToPosition(0)
            }
        }
    }

    private fun observeEffect() {
        collect(flow = notificationViewModel.effects) { effect ->
            when (effect) {
                is NotificationEffect.ShowErrorMessage -> binding.root.showSnackbar(effect.errorMessageRes)
                is NotificationEffect.NavigateToPost -> navigateToPost(
                    postId = effect.postId,
                    showComments = effect.isComment
                )
                is NotificationEffect.NavigateToUserProfile -> navigateToProfile(userId = effect.userId)
            }
        }
    }

    private fun setReactionsSwipeListener() {
        binding.swipeRefreshLayoutNotifications.setOnRefreshListener {
            notificationViewModel.onEvent(event = NotificationEvent.GetNotifications(refresh = true))
        }
    }

    private fun navigateToPost(postId: String, showComments: Boolean) {
        val action = MainFragmentDirections.actionMainFragmentToPostFragment(
            postId = postId,
            showComments = showComments
        )
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun navigateToProfile(userId: String) {
        val action = MainFragmentDirections.actionMainFragmentToProfileFragment(
            userId = userId
        )
        requireActivity().findNavController(R.id.fragmentContainerView).navigate(action)
    }

    private fun onReactNotificationSelected(postId: String, isComment: Boolean) {
        notificationViewModel.onEvent(
            event = NotificationEvent.PostNotificationSelected(
                postId = postId,
                isComment = isComment
            )
        )
    }

    private fun setUpNotificationAdapter() {
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = notificationAdapter
    }

}