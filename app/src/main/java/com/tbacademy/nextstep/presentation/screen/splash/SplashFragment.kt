package com.tbacademy.nextstep.presentation.screen.splash

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
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
                applySystemTheme(state.theme)

                // Apply Language
                applyLanguage(requireContext(), state.language)
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

        val newContext = context.createConfigurationContext(config)

        if (context is android.app.Activity) {
            context.applyOverrideConfiguration(config)
        }
    }

}