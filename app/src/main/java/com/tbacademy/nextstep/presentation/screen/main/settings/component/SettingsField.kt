package com.tbacademy.nextstep.presentation.screen.main.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tbacademy.nextstep.presentation.components.dropdown.Dropdown

@Composable
fun SettingsField(
    title: String,
    options: List<String>,
    selectedOption: String,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    onOptionSelected: (String) -> Unit,
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
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Dropdown(
            title = "", // empty because field already shows title
            options = options,
            selectedOption = selectedOption,
            expanded = expanded,
            onExpandToggle = onExpandToggle,
            onOptionSelected = onOptionSelected
        )
    }
}