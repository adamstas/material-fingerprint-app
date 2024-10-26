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

//inspired by https://stackoverflow.com/questions/75397960/jetpack-compose-combo-box-with-dropdown //todo nechat? zminit?
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithCheckboxes(
    label: String,
    options: List<DropDownMenuWithCheckboxesItem>,
    //onOptionsChosen: (List<ComboOption>) -> Unit,
    modifier: Modifier = Modifier, //todo nechat?
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
        //  if (!expanded) {
        // onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
        //todo jakmile zavre menu, tato logika se provede s temi zaskrtnutymi (predat funkci teto composable, at je to znovupouzitelne)
        //todo sem dat logiku ze se do noveho listu ulozi selectovana idcka a teprv na zaklade nej se updatuje _materials list atd.
        //zaskrtnute ziskat nejak takto: options.filter { it.id in selectedOptionsList }.toList()
        // }
        ,
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
            //{
            //expanded = false
            //onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
            //todo jakmile zavre menu, tato logika se provede s temi zaskrtnutymi (predat funkci teto composable, at je to znovupouzitelne)
            //ale mozna tahle logika staci jen výš, otestovat
            //zaskrtnute ziskat nejak takto: options.filter { it.id in selectedOptionsList }.toList()
            //},
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