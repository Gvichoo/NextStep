package com.tbacademy.nextstep.presentation.common.compose.components.dropdown

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tbacademy.nextstep.presentation.theme.NexStepAppTheme

@Composable
fun <T> Dropdown(
    title: String,
    options: List<T>,
    selectedOption: T,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    modifier: Modifier = Modifier
) {
    NexStepAppTheme {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )

            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExpandToggle() }
                    .padding(horizontal = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = optionLabel(selectedOption),
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                options.forEach { option ->
                    Text(
                        text = optionLabel(option),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                onExpandToggle()
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun DropdownPreview() {
    var selectedOption by remember { mutableStateOf("System") }
    var expanded by remember { mutableStateOf(false) }

    Dropdown(
        title = "Select Theme",
        options = listOf("System", "Light", "Dark"),
        selectedOption = selectedOption,
        expanded = expanded,
        onExpandToggle = { expanded = !expanded },
        onOptionSelected = { selectedOption = it },
        optionLabel = { it }
    )
}