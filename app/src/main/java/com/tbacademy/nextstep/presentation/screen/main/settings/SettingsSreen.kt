package com.tbacademy.nextstep.presentation.screen.main.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tbacademy.nextstep.presentation.screen.main.settings.state.SettingsState

@Composable
fun SettingsScreen(
    state: SettingsState,
    onChangeLanguage: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (state.language == "ka") "პარამეტრები" else "Settings",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { onChangeLanguage("en") }) {
                Text("Switch to English")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { onChangeLanguage("ka") }) {
                Text("Switch to Georgian")
            }
        }
    }
}