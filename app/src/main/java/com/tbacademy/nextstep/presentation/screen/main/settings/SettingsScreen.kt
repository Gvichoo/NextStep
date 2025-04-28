package com.tbacademy.nextstep.presentation.screen.main.settings

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbacademy.nextstep.presentation.components.title.ScreenTitle
import com.tbacademy.nextstep.presentation.screen.main.settings.component.SettingsField
import com.tbacademy.nextstep.presentation.screen.main.settings.event.SettingsEvent
import com.tbacademy.nextstep.presentation.screen.main.settings.state.SettingsState

@Composable
fun SettingsScreen(
    state: SettingsState,
    onEvent: (SettingsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
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
            options = listOf("System", "Light", "Dark"),
            selectedOption = state.selectedTheme.toDisplayString(),
            expanded = state.isThemeDropdownExpanded,
            onExpandToggle = {
                onEvent(SettingsEvent.ToggleThemeDropdown)
            },
            onOptionSelected = { selected ->
                onEvent(SettingsEvent.ThemeSelected(selected.toAppTheme()))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        SettingsField(
            title = "Language",
            options = listOf("System", "English", "Georgian"),
            selectedOption = state.selectedLanguage.toDisplayString(),
            expanded = state.isLanguageDropdownExpanded,
            onExpandToggle = {
                onEvent(SettingsEvent.ToggleLanguageDropdown)
            },
            onOptionSelected = { selected ->
                onEvent(SettingsEvent.LanguageSelected(selected.toAppLanguage()))
            }
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


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        state = SettingsState(),
        onEvent = {}
    )
}