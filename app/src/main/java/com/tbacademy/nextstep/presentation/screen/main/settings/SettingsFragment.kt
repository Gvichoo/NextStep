package com.tbacademy.nextstep.presentation.screen.main.settings

import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentSettingsBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.screen.main.main_screen.MainFragmentDirections
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun start() {
        binding.composeView.setContent {
            SettingsScreen()
        }
    }

    override fun listeners() {
        setLogoutBtnListener()
    }

    override fun observers() {
        observeEffect()
    }

    private fun observeEffect() {
        collect(flow = settingsViewModel.effects) { effect ->
            when (effect) {
                is SettingsEffect.NavigateToLogin -> navigateToLogin()
            }
        }
    }

    private fun setLogoutBtnListener() {
    }

    private fun navigateToLogin() {
        val navController = requireActivity().findNavController(R.id.fragmentContainerView)
        val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
        navController.navigate(action)
    }
}