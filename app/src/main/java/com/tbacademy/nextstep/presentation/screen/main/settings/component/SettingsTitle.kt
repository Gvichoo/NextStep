package com.tbacademy.nextstep.presentation.screen.main.settings.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


@Composable
fun SettingsTitle() {
    Text(
        text = "Settings",
        style = MaterialTheme.typography.headlineMedium,
    )
}