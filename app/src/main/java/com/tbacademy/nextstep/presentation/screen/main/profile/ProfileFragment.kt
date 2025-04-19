package com.tbacademy.nextstep.presentation.screen.main.profile

import android.provider.ContactsContract.Profile
import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.databinding.FragmentProfileBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.profile.event.ProfileEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val profileViewModel: ProfileViewModel by viewModels()

    override fun start() {
        checkProfileOwner()
    }

    override fun listeners() {
        setOnBackBtnListener()
        setFollowBtnListener()
    }

    override fun observers() {
        observeState()
    }

    private fun observeState() {
        collectLatest(flow = profileViewModel.state) { state ->
            binding.apply {
                pbProfile.isVisible = state.isLoading
                groupProfileContent.isVisible = !state.isLoading

                if (state.user != null) {
                    tvUsername.text = state.user.username
                    btnFollow.isVisible = !state.isOwnProfile
                    btnFollow.text = requireContext().getString(state.isUserFollowed.textRes)
                    btnBack.isVisible = !state.isOwnProfile
                }
            }
            Log.d("PROFILE_STATE", "$state")
        }
    }

    private fun setOnBackBtnListener() {
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setFollowBtnListener() {
        binding.btnFollow.setOnClickListener {
            profileViewModel.onEvent(ProfileEvent.ToggleFollowUser)
        }
    }

    private fun checkProfileOwner() {
        val usedId = arguments?.getString("userId")
        profileViewModel.onEvent(ProfileEvent.SetProfileState(userId = usedId))
    }
}