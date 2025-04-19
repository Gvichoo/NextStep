package com.tbacademy.nextstep.presentation.screen.main.profile

import android.util.Log
import androidx.fragment.app.viewModels
import com.tbacademy.nextstep.databinding.FragmentProfileBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.profile.event.ProfileEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun start() {
        profileViewModel.onEvent(ProfileEvent.FetchUserInfo())
    }

    override fun listeners() {

    }

    override fun observers() {
        observeState()
    }

    private fun observeState() {
        collectLatest(flow = profileViewModel.state) { state ->
            binding.apply {
                tvUsername.text = state.user?.username
            }
            Log.d("PROFILE_STATE", "$state")
        }
    }
}