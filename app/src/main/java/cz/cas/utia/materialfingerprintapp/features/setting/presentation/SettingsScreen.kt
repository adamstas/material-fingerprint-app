package cz.cas.utia.materialfingerprintapp.features.setting.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.CustomAppTheme

@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            TopBarTitle(title = "Settings")
            }
        )

    { paddingValues -> //todo proc padding nefunguje? Je to OK, bez toho by to bylo prekryte tou horni listou
        Column(
            Modifier
                .padding(paddingValues)
                .padding(25.dp)
               //.fillMaxSize() //todo ma to nejaky vliv?
        )
        {
            SendDataSection()

            CustomHorizontalDivider()

            DefaultWindowSection()

            CustomHorizontalDivider()

            Text(text = "Some other settings ...")
        }
    }
}

@Composable
fun SendDataSection() {
    var checked by remember { mutableStateOf(true) } //just for temporary switch

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Send data to the server")
            Text(text = "(anonymously)")
        }

        Switch(checked = checked, onCheckedChange = {
            checked = it
        })
    }
}

@Composable
fun CustomHorizontalDivider() {
    HorizontalDivider(
        thickness = 1.5.dp, //todo thickness upravit? 1.5 asi OK
        modifier = Modifier.padding(vertical = 20.dp)
        )
}

@Composable
fun DefaultWindowSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Default window")
        DefaultWindowExposedDropdownMenu()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultWindowExposedDropdownMenu() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) } //todo zkusit kdyby bez remember
    val items = listOf("Camera", "Analytics", "Settings", "Last opened window")
    var selectedItem by remember { mutableStateOf(items[0]) } //todo bez remember by si to nepamatoval pri odchodu z obrazovky?

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.width(225.dp) //todo keep hardcoded? mozna na uzsich obrazovkach to bude delat problem => zkusit a kdyz tak to lze dát na samostatny radek
    ) {
        TextField(
            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

            items.forEach { item ->
                DropdownMenuItem(text = { Text(text = item) }, onClick = {
                    selectedItem = item
                    expanded = false
                    Toast.makeText(context, item, Toast.LENGTH_SHORT).show() //todo dat Toast nebo bez nej?
                })
            }
        }

    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    CustomAppTheme {
        SettingsScreen()
    }
}
