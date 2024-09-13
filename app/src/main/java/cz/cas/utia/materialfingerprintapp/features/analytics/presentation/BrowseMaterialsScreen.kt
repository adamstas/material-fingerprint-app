package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.CustomAppTheme
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.Permission
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.SettingsScreen
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.permissions

@Composable
fun BrowseMaterialsScreen() {
    Scaffold(
        topBar = {
            BackTopBarTitle(title = "Browse materials") //todo rozlisit jestli jsou to lokal/online data?
        }
    ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(25.dp)
            ) {
                SearchAndFilterSection()

            }
    }
}

@Composable
fun SearchAndFilterSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MaterialsSearchBar()
        CategoriesTopDownMenu()
    }
}

@Composable
fun MaterialsSearchBar() {
    //todo finish this
    Text("I will be a search bar")
    //maybe put the search bar up the categories top down menu in case the top down menu is too wide
}

data class CategoryWithCheckbox(
    val category: MaterialCategory,
    val isChecked: Boolean
)

//todo use https://stackoverflow.com/questions/75397960/jetpack-compose-combo-box-with-dropdown and create it like in the tutorial
//then finish this screen just with dummy data and then finish the database
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesTopDownMenu() {
        var expanded by remember { mutableStateOf(false) } //todo zkusit kdyby bez remember
        val categories = MaterialCategory.entries

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.width(225.dp) //todo keep hardcoded? mozna na uzsich obrazovkach to bude delat problem => zkusit a kdyz tak to lze dát na samostatny radek
            //anebo nastavit size namisto width, to by asi melo zmensit cele => treba to bude vypadat lepe
        ) {
            TextField(
                value = "Categories",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

                categories.forEach { item ->
                    Row {
                        DropdownMenuItem(text = { Text(text = item.toString()) }, onClick = {
                            //selectedItem = item
                            //expanded = false
                        })
                        Checkbox(checked = true, onCheckedChange = {})
                    }


                }
            }

        }
}


@Preview
@Composable
fun BrowseMaterialsScreenPreview() {
    CustomAppTheme {
        BrowseMaterialsScreen()
    }
}