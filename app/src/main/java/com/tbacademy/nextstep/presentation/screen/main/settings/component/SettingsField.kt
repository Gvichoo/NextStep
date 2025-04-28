package com.tbacademy.nextstep.presentation.screen.main.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tbacademy.nextstep.presentation.common.compose.components.dropdown.Dropdown

@Composable
fun <T> SettingsField(
    title: String,
    options: List<T>,
    selectedOption: T,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Dropdown(
            title = "", // empty because field already shows title
            options = options,
            selectedOption = selectedOption,
            expanded = expanded,
            onExpandToggle = onExpandToggle,
            onOptionSelected = onOptionSelected,
            optionLabel = optionLabel
        )
    }
}