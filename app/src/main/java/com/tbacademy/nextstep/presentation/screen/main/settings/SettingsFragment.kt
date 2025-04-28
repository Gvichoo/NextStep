package com.tbacademy.nextstep.presentation.screen.main.settings

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentSettingsBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.screen.main.main_screen.MainFragmentDirections
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun start() {
        binding.composeView.setContent {
            val state by settingsViewModel.state.collectAsState()
            Log.d("STATE_SETTINGS", "$state")

            SettingsScreen(
                state = state,
                onEvent = { settingsViewModel.onEvent(it) }
            )
        }
    }

    override fun listeners() {
    }

    override fun observers() {
        observeEffect()
    }

    private fun observeEffect() {
        collect(flow = settingsViewModel.effects) { effect ->
            when (effect) {
                is SettingsEffect.NavigateToLogin -> navigateToLogin()
                is SettingsEffect.ShowErrorMessage -> {}
                is SettingsEffect.ShowSuccessMessage -> {}
                is SettingsEffect.ApplyTheme -> {
                    applySystemTheme(theme = effect.theme)
                    requireActivity().recreate()
                }
                is SettingsEffect.ApplyLanguage -> {
                    applyLanguage(context = requireContext(), effect.language)
                    requireActivity().recreate()
                }
            }
        }
    }

    private fun applySystemTheme(theme: AppThemePresentation) {
        when (theme) {
            AppThemePresentation.SYSTEM -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
            AppThemePresentation.LIGHT -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            AppThemePresentation.DARK -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }
    private fun applyLanguage(context: Context, language: AppLanguagePresentation) {
        val locale = when (language) {
            AppLanguagePresentation.SYSTEM -> Locale.getDefault() // use system
            AppLanguagePresentation.EN -> Locale("en")
            AppLanguagePresentation.KA -> Locale("ka")
        }

        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }


    private fun navigateToLogin() {
        val navController = requireActivity().findNavController(R.id.fragmentContainerView)
        val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
        navController.navigate(action)
    }
}