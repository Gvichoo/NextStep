package com.tbacademy.nextstep.presentation.screen.main.user_search

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tbacademy.nextstep.databinding.FragmentUserSearchBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.user_search.event.UserSearchEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSearchFragment : BaseFragment<FragmentUserSearchBinding>(FragmentUserSearchBinding::inflate) {

    private val userSearchAdapter by lazy {
        UserSearchAdapter()
    }

    private val userSearchViewModel: UserSearchViewModel by viewModels()

    override fun start() {
        setAdapter()
    }

    override fun listeners() {
        setSearchInputListener()
    }

    override fun observers() {
        observeState()
    }

    private fun setSearchInputListener() {
        binding.etSearch.onTextChanged { value ->
            userSearchViewModel.onEvent(UserSearchEvent.QueryChanged(value = value))
        }
    }

    private fun observeState() {
        collectLatest(flow = userSearchViewModel.state) { state ->
            userSearchAdapter.submitList(state.users)
            binding.pbSearch.isVisible = state.loading
        }
    }

    private fun setAdapter() {
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = userSearchAdapter
    }
}