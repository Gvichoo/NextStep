package com.tbacademy.nextstep.presentation.screen.main.settings

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.tbacademy.nextstep.R
import com.tbacademy.nextstep.databinding.FragmentSettingsBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.screen.main.main_screen.MainFragmentDirections
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
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

                is SettingsEffect.ApplyLanguage -> {
                    applyLanguage(effect.language)
                }
            }
        }
    }

    private fun applyLanguage(language: AppLanguagePresentation) {
        val languageTag = when (language) {
            AppLanguagePresentation.SYSTEM -> ""
            AppLanguagePresentation.EN     -> "en"
            AppLanguagePresentation.KA     -> "ka"
        }

        if (languageTag.isNotBlank()) {
            Locale.setDefault(Locale.forLanguageTag(languageTag))
        }

        val locales = if (languageTag.isBlank()) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(languageTag)
        }

        AppCompatDelegate.setApplicationLocales(locales)

        requireActivity().recreate()
    }


    private fun navigateToLogin() {
        val navController = requireActivity().findNavController(R.id.fragmentContainerView)
        val action = MainFragmentDirections.actionMainFragmentToLoginFragment()
        navController.navigate(action)
    }
}