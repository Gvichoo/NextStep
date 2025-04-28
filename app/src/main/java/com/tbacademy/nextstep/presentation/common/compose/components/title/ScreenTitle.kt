package com.tbacademy.nextstep.presentation.common.compose.components.title

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Start,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
            .padding(top = 24.dp)
    )
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun ScreenTitlePreview() {
    ScreenTitle(text = "Settings")
}