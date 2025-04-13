package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

data class DropDownMenuWithCheckboxesItem(
    val text: String,
    val id: Int
)

//inspired by https://stackoverflow.com/questions/75397960/jetpack-compose-combo-box-with-dropdown
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithCheckboxes(
    label: String,
    options: List<DropDownMenuWithCheckboxesItem>,
    selectedIDs: List<Int> = emptyList(),
    selectedText: String,
    checkOrUncheckItem: (Int) -> Unit,
    expanded: Boolean,
    onDropdownMenuClick: (Boolean) -> Unit,
    onDropdownMenuClosed: () -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onDropdownMenuClick
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedText,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = onDropdownMenuClosed
        ) {
            for (option in options) {
                val checked = option.id in selectedIDs
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = {
                                    checkOrUncheckItem(option.id)
                                },
                            )
                            Text(text = option.text)
                        }
                    },
                    onClick = {
                        checkOrUncheckItem(option.id)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}