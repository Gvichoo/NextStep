package com.tbacademy.nextstep.presentation.screen.main.settings

import com.tbacademy.core.model.Resource
import com.tbacademy.core.model.error.ApiError
import com.tbacademy.nextstep.MainDispatcherRule
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val logoutUseCase = mockk<LogoutUserUseCase>()
    private val saveValueUseCase = mockk<SaveValueToPreferencesStorageUseCase>()
    private val readValueUseCase = mockk<ReadValueFromPreferencesStorageUseCase>()

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        // default DataStore returns null for both settings
        every { readValueUseCase(AppPreferenceKeys.LANGUAGE_KEY) } returns flowOf(null)
        every { readValueUseCase(AppPreferenceKeys.KEY_THEME_MODE) } returns flowOf(null)

        viewModel = SettingsViewModel(
            logoutUseCase,
            saveValueUseCase,
            readValueUseCase
        )
    }

    @Test
    fun `toggle theme dropdown flips the flag`() {
        // Arrange
        val initial = viewModel.state.value.isThemeDropdownExpanded
        // Act
        viewModel.onEvent(SettingsEvent.ToggleThemeDropdown)
        // Assert
        assertEquals(!initial, viewModel.state.value.isThemeDropdownExpanded)
    }

    @Test
    fun `toggle language dropdown flips the flag`() {
        // Arrange
        val initial = viewModel.state.value.isLanguageDropdownExpanded
        // Act
        viewModel.onEvent(SettingsEvent.ToggleLanguageDropdown)
        // Assert
        assertEquals(!initial, viewModel.state.value.isLanguageDropdownExpanded)
    }

    @Test
    fun `select theme updates state and saves preference`() = runTest {
        // Arrange
        coEvery { saveValueUseCase(AppPreferenceKeys.KEY_THEME_MODE, AppThemePresentation.DARK.name) } just Runs
        // Act
        viewModel.onEvent(SettingsEvent.ThemeSelected(AppThemePresentation.DARK))
        advanceUntilIdle()
        // Assert
        assertEquals(AppThemePresentation.DARK, viewModel.state.value.selectedTheme)
        assertFalse(viewModel.state.value.isThemeDropdownExpanded)
    }

    @Test
    fun `select language updates state, saves preference, and emits effect`() = runTest {
        // Arrange
        coEvery { saveValueUseCase(AppPreferenceKeys.LANGUAGE_KEY, AppLanguagePresentation.KA.name) } just Runs

        // Act
        viewModel.onEvent(SettingsEvent.LanguageSelected(AppLanguagePresentation.KA))
        advanceUntilIdle()

        // Assert state
        assertEquals(AppLanguagePresentation.KA, viewModel.state.value.selectedLanguage)
        assertFalse(viewModel.state.value.isLanguageDropdownExpanded)

        // Assert effect from buffered Channel
        val effect = viewModel.effects.first()
        assertEquals(SettingsEffect.ApplyLanguage(AppLanguagePresentation.KA), effect)
    }

    @Test
    fun `logout success emits NavigateToLogin effect`() = runTest {
        // Arrange
        coEvery { logoutUseCase() } returns flowOf(Resource.Success(Unit))
        // Act
        viewModel.onEvent(SettingsEvent.Logout)
        advanceUntilIdle()
        // Assert
        val effect = viewModel.effects.first()
        assertEquals(SettingsEffect.NavigateToLogin, effect)
    }

    @Test
    fun `logout error emits NavigateToLogin effect`() = runTest {
        // Arrange
        coEvery { logoutUseCase() } returns flowOf(Resource.Error(ApiError.Unknown()))
        // Act
        viewModel.onEvent(SettingsEvent.Logout)
        advanceUntilIdle()
        // Assert
        val effect = viewModel.effects.first()
        assertEquals(SettingsEffect.NavigateToLogin, effect)
    }

    @Test
    fun `restoreLocalSettings sets default SYSTEM values when datastore returns null`() {
        // Arrange done in setUp
        // Act & Assert
        assertEquals(AppLanguagePresentation.SYSTEM, viewModel.state.value.selectedLanguage)
        assertEquals(AppThemePresentation.SYSTEM, viewModel.state.value.selectedTheme)
    }

    @Test
    fun `restoreLocalSettings sets saved language and theme from datastore`() = runTest {
        // Arrange stored values
        every { readValueUseCase(AppPreferenceKeys.LANGUAGE_KEY) } returns flowOf(AppLanguagePresentation.EN.name)
        every { readValueUseCase(AppPreferenceKeys.KEY_THEME_MODE) } returns flowOf(AppThemePresentation.LIGHT.name)
        // Recreate to trigger init
        viewModel = SettingsViewModel(logoutUseCase, saveValueUseCase, readValueUseCase)

        // Act
        advanceUntilIdle()

        // Assert
        assertEquals(AppLanguagePresentation.EN, viewModel.state.value.selectedLanguage)
        assertEquals(AppThemePresentation.LIGHT, viewModel.state.value.selectedTheme)
    }
}