package com.tbacademy.nextstep.presentation.screen.splash

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tbacademy.nextstep.databinding.FragmentSplashBinding
import com.tbacademy.nextstep.presentation.base.BaseFragment
import com.tbacademy.nextstep.presentation.extension.collect
import com.tbacademy.nextstep.presentation.extension.collectLatest
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import com.tbacademy.nextstep.presentation.screen.splash.effect.SplashEffect
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

//Bla Bla Splash
@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val splashViewModel: SplashViewModel by viewModels()

    override fun start() {}
    override fun listeners() {}

    override fun observers() {
        observeState()
        observeEffect()
    }

    private fun observeEffect() {
        collect(flow = splashViewModel.effect) { effect ->
            when (effect) {
                is SplashEffect.NavigateToLogin -> {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    )
                }

                is SplashEffect.NavigateToMain -> {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToMainFragment()
                    )
                }
            }
        }
    }

    private fun observeState() {
        collectLatest(flow = splashViewModel.state) { state ->
            if (state.isReady) {
                // Apply Theme
                applySystemTheme(theme = state.theme)

                // Apply Language
                applyLanguage(language = state.language)
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
}