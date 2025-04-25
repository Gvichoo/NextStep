package com.tbacademy.nextstep.presentation.screen.main.notification

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbacademy.nextstep.databinding.FragmentNotificationBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.notification.event.NotificationEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val notificationViewModel: NotificationViewModel by viewModels()

    private val notificationAdapter by lazy {
        NotificationAdapter()
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

    private fun setReactionsSwipeListener() {
        binding.swipeRefreshLayoutNotifications.setOnRefreshListener {
            notificationViewModel.onEvent(event = NotificationEvent.GetNotifications(refresh = true))
        }
    }

    private fun setUpNotificationAdapter() {
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = notificationAdapter
    }

}