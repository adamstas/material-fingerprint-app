package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SingleChoiceSegmentedButton(
    selectedOption: T,
    options: List<T>,
    onSelectionChange: (T) -> Unit,
    enabled: Boolean,
    label: (T) -> String = { it.toString() }
) {
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onSelectionChange(option)},
                selected = selectedOption == option,
                modifier = Modifier.alpha(if (enabled) 1f else 0.5f),
                label = { Text(label(option)) },
                enabled = enabled
            )
        }
    }
}