package com.tbacademy.nextstep.presentation.screen.main.user_search

import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tbacademy.nextstep.databinding.FragmentUserSearchBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.extension.onTextChanged
import com.tbacademy.nextstep.presentation.screen.main.user_search.effect.UserSearchEffect
import com.tbacademy.nextstep.presentation.screen.main.user_search.event.UserSearchEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserSearchFragment :
    BaseFragment<FragmentUserSearchBinding>(FragmentUserSearchBinding::inflate) {

    private val userSearchAdapter by lazy {
        UserSearchAdapter(
            onUserClicked = { userId ->
                userSearchViewModel.onEvent(UserSearchEvent.UserSelected(userId = userId))
            }
        )
    }

    private val userSearchViewModel: UserSearchViewModel by viewModels()

    override fun start() {
        setAdapter()
    }

    override fun listeners() {
        setSearchInputListener()
        setBackBtnListener()
    }

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun setSearchInputListener() {
        binding.etSearch.onTextChanged { value ->
            userSearchViewModel.onEvent(UserSearchEvent.QueryChanged(value = value))
        }
    }

    private fun observeEffect() {
        collect(flow = userSearchViewModel.effects) { effect ->
            when (effect) {
                is UserSearchEffect.NavigateToProfile -> navigateToProfile(userId = effect.userId)
                is UserSearchEffect.NavigateBack -> findNavController().navigateUp()
                is UserSearchEffect.ShowMessage -> showMessage(message = effect.messageRes)
            }
        }
    }

    private fun observeState() {
        collectLatest(flow = userSearchViewModel.state) { state ->
            userSearchAdapter.submitList(state.users)
            binding.pbSearch.isVisible = state.loading
        }
    }

    private fun setBackBtnListener() {
        binding.btnBack.setOnClickListener {
            userSearchViewModel.onEvent(UserSearchEvent.BackRequest)
        }
    }

    private fun navigateToProfile(userId: String) {
        val action = UserSearchFragmentDirections.actionUserSearchFragmentToProfileFragment(
            userId = userId
        )
        findNavController().navigate(action)
    }

    private fun setAdapter() {
        binding.rvUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUsers.adapter = userSearchAdapter
    }

    private fun showMessage(message: Int) {
        view?.let {
            Snackbar.make(it, getString(message), Snackbar.LENGTH_SHORT).show()
        }
    }
}