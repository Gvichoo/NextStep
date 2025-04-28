package com.tbacademy.nextstep

import com.tbacademy.core.Resource
import com.tbacademy.nextstep.domain.manager.preferences.AppPreferenceKeys
import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.SettingsViewModel
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import com.tbacademy.nextstep.presentation.screen.main.settings.effect.SettingsEffect
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


@ExperimentalCoroutinesApi
class SettingsViewModelTest {

    private lateinit var viewModel: SettingsViewModel
    private val logoutUserUseCase: LogoutUserUseCase = mockk()
    private val saveValueUseCase: SaveValueToPreferencesStorageUseCase = mockk(relaxed = true)
    private val readValueUseCase: ReadValueFromPreferencesStorageUseCase = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { readValueUseCase.invoke(AppPreferenceKeys.LANGUAGE_KEY) } returns flow { emit("en") }
        viewModel = SettingsViewModel(logoutUserUseCase, saveValueUseCase, readValueUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `changeLanguage saves language and updates viewState`() = runTest {
        // Given
        val newLanguage = "ka" // example new language

        // Collect the emitted effects first
        val emittedEffect = mutableListOf<SettingsEffect>()
        val job = launch {
            viewModel.effects.collect {
                emittedEffect.add(it)
            }
        }

        // When
        viewModel.onEvent(SettingsEvent.ChangeLanguage(newLanguage)) // Trigger the event
        advanceUntilIdle()  // Allow coroutine to finish

        // Then
        // Verify that the language has been updated in the view state
        assertEquals(newLanguage, viewModel.state.value.language)

        // Verify that the SaveValueToPreferencesStorageUseCase was called with the correct parameters
        coVerify { saveValueUseCase.invoke(AppPreferenceKeys.LANGUAGE_KEY, newLanguage) }

        // Check if any effects were emitted (in this case, none are expected)
        assert(emittedEffect.isEmpty())

        job.cancel()  // Cancel the job after collecting the effects
    }

    @Test
    fun `initial language is loaded correctly`() = runTest {
        // Given -> in setUp() already emits "en" as the initial language

        // When
        advanceUntilIdle()  // Allow coroutines to run

        // Then
        // Verify that the initial language is "en"
        assertEquals("en", viewModel.state.value.language)
    }
}

