package com.tbacademy.nextstep

import com.tbacademy.core.model.Resource
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.SettingsViewModel
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppLanguagePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    private val logoutUseCase: LogoutUserUseCase = mockk()
    private val saveValueUseCase: SaveValueToPreferencesStorageUseCase = mockk(relaxed = true)
    private val readValueUseCase: ReadValueFromPreferencesStorageUseCase = mockk()
    private lateinit var viewModel: SettingsViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { readValueUseCase(any()) } returns flowOf(null)
        viewModel = SettingsViewModel(logoutUseCase, saveValueUseCase, readValueUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `toggle theme dropdown updates state`() = runTest {
        val initial = viewModel.state.value.isThemeDropdownExpanded
        viewModel.onEvent(SettingsEvent.ToggleThemeDropdown)
        assertEquals(!initial, viewModel.state.value.isThemeDropdownExpanded)
    }

    @Test
    fun `toggle language dropdown updates state`() = runTest {
        val initial = viewModel.state.value.isLanguageDropdownExpanded
        viewModel.onEvent(SettingsEvent.ToggleLanguageDropdown)
        assertEquals(!initial, viewModel.state.value.isLanguageDropdownExpanded)
    }

    @Test
    fun `select theme updates state and emits effect`() = runTest {
        val theme = AppThemePresentation.DARK
        val job = launch {
            viewModel.effects.test {
                viewModel.onEvent(SettingsEvent.ThemeSelected(theme))
                testDispatcher.scheduler.advanceUntilIdle()

                assertEquals(theme, viewModel.state.value.selectedTheme)
                assertFalse(viewModel.state.value.isThemeDropdownExpanded)
                assertEquals(SettingsEffect.ApplyTheme(theme), awaitItem())
            }
        }
        job.cancel()
    }

    @Test
    fun `select language updates state and emits effect`() = runTest {
        val lang = AppLanguagePresentation.KA
        val job = launch {
            viewModel.effects.test {
                viewModel.onEvent(SettingsEvent.LanguageSelected(lang))
                testDispatcher.scheduler.advanceUntilIdle()

                assertEquals(lang, viewModel.state.value.selectedLanguage)
                assertFalse(viewModel.state.value.isLanguageDropdownExpanded)
                assertEquals(SettingsEffect.ApplyLanguage(lang), awaitItem())
            }
        }
        job.cancel()
    }

    @Test
    fun `logout emits NavigateToLogin on success`() = runTest {
        coEvery { logoutUseCase() } returns flowOf(Resource.Success(Unit))

        val job = launch {
            viewModel.effects.test {
                viewModel.onEvent(SettingsEvent.Logout)
                testDispatcher.scheduler.advanceUntilIdle()

                assertEquals(SettingsEffect.NavigateToLogin, awaitItem())
            }
        }
        job.cancel()
    }

    @Test
    fun `logout emits NavigateToLogin on error`() = runTest {
        coEvery { logoutUseCase() } returns flowOf(Resource.Error("fail"))

        val job = launch {
            viewModel.effects.test {
                viewModel.onEvent(SettingsEvent.Logout)
                testDispatcher.scheduler.advanceUntilIdle()

                assertEquals(SettingsEffect.NavigateToLogin, awaitItem())
            }
        }
        job.cancel()
    }

    @Test
    fun `restoreLocalSettings sets default values when null`() = runTest {
        viewModel = SettingsViewModel(logoutUseCase, saveValueUseCase, readValueUseCase)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(AppLanguagePresentation.SYSTEM, viewModel.state.value.selectedLanguage)
        assertEquals(AppThemePresentation.SYSTEM, viewModel.state.value.selectedTheme)
    }
}