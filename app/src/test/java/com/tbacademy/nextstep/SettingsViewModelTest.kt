package com.tbacademy.nextstep

import com.tbacademy.nextstep.domain.usecase.login.LogoutUserUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.ReadValueFromPreferencesStorageUseCase
import com.tbacademy.nextstep.domain.usecase.preferences.SaveValueToPreferencesStorageUseCase
import com.tbacademy.nextstep.presentation.screen.main.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
    fun `logout success emits NavigateToLogin effect`() = runTest {
        // Given
        coEvery { logoutUserUseCase.invoke() } returns flow { emit(Resource.Success(Unit)) }

        // When
        viewModel.onEvent(SettingsEvent.Logout())
        advanceUntilIdle()

        // Then
        val effect = viewModel.effect.value
        assertEquals(SettingsEffect.NavigateToLogin, effect)
    }

    @Test
    fun `logout error still emits NavigateToLogin effect`() = runTest {
        // Given
        coEvery { logoutUserUseCase.invoke() } returns flow { emit(Resource.Error<Unit>(Throwable("Logout failed"))) }

        // When
        viewModel.onEvent(SettingsEvent.Logout())
        advanceUntilIdle()

        // Then
        val effect = viewModel.effect.value
        assertEquals(SettingsEffect.NavigateToLogin, effect)
    }

    @Test
    fun `changeLanguage saves language and updates viewState`() = runTest {
        // Given
        val newLanguage = "ka" // example

        // When
        viewModel.onEvent(SettingsEvent.ChangeLanguage(newLanguage))
        advanceUntilIdle()

        // Then
        assertEquals(newLanguage, viewModel.viewState.value.language)
        verify { saveValueUseCase.invoke(AppPreferenceKeys.LANGUAGE_KEY, newLanguage) }
    }

    @Test
    fun `initial language is loaded correctly`() = runTest {
        // Given -> in setUp() already emits "en"

        // When
        advanceUntilIdle()

        // Then
        assertEquals("en", viewModel.viewState.value.language)
    }
}