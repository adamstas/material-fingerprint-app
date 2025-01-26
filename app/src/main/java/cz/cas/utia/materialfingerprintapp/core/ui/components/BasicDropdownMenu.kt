package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

//todo pak pouzit i na settings screeně
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> BasicDropdownMenu(
    expanded: Boolean,
    onDropdownMenuClick: (Boolean) -> Unit,
    onDropdownMenuClosed: () -> Unit,
    onDropdownMenuItemClick: (T) -> Unit,
    options: List<T>,
    selectedOption: T,
    modifier: Modifier = Modifier
) {
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = onDropdownMenuClick,
        modifier = modifier
    ) {
        TextField(
            value = selectedOption.toString(),
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { onDropdownMenuClosed() })
        {

            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.toString()) },
                    onClick = {
                        onDropdownMenuItemClick(option)
                        onDropdownMenuClosed()
                    }
                )
            }
        }
    }
}