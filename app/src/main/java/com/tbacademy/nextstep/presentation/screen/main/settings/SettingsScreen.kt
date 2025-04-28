package com.tbacademy.nextstep.presentation.screen.main.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbacademy.nextstep.presentation.common.compose.components.title.ScreenTitle
import com.tbacademy.nextstep.presentation.screen.main.settings.component.SettingsField
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import com.tbacademy.nextstep.presentation.screen.main.settings.model.AppThemePresentation
import com.tbacademy.nextstep.presentation.screen.main.settings.state.SettingsState
import com.tbacademy.nextstep.presentation.theme.NexStepAppTheme

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    NexStepAppTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title
            ScreenTitle(text = "Settings")

            Spacer(modifier = Modifier.height(32.dp))

            SettingsField(
                title = "App Theme",
                options = state.appThemeOptions,
                selectedOption = state.selectedTheme,
                expanded = state.isThemeDropdownExpanded,
                onExpandToggle = {
                    onEvent(SettingsEvent.ToggleThemeDropdown)
                },
                onOptionSelected = { selected ->
                    onEvent(SettingsEvent.ThemeSelected(selected))
                },
                optionLabel = { stringResource(id = it.titleRes) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingsField(
                title = "Language",
                options = state.appLanguageOptions,
                selectedOption = state.selectedLanguage,
                expanded = state.isLanguageDropdownExpanded,
                onExpandToggle = {
                    onEvent(SettingsEvent.ToggleLanguageDropdown)
                },
                onOptionSelected = { selected ->
                    onEvent(SettingsEvent.LanguageSelected(language = selected))
                },
                optionLabel = { stringResource(id = it.titleRes) }
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Logout Button
            Button(
                onClick = { onEvent(SettingsEvent.Logout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Log Out",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    apiLevel = 34,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun SettingsScreenLightPreview() {
    SettingsScreen(
        state = SettingsState(),
        onEvent = {}
    )
}

@Preview(
    name = "Dark Mode",
    showBackground = true,
    apiLevel = 34,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SettingsScreenDarkPreview() {
    SettingsScreen(
        state = SettingsState().copy(selectedTheme = AppThemePresentation.DARK),
        onEvent = {}
    )
}