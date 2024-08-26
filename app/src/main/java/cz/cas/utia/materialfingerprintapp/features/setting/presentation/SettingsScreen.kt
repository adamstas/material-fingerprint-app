package cz.cas.utia.materialfingerprintapp.features.setting.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var expanded by remember { mutableStateOf(true) } //todo zkusit kdyby bez remember
    val items = listOf("A", "B", "C", "D", "E", "F")
    val disabledValue = "B"
    var selectedIndex by remember { mutableStateOf(0) } //todo bez remember by si to nepamatoval pri odchodu z obrazovky?

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Default window")

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {

            items.forEach {
                DropdownMenuItem(text = {Text(text = "item $it")}, onClick = { /* todo */})

            }
        }
    }
}

//@Composable
//fun DropdownDemo() {
//    var expanded by remember { mutableStateOf(false) }
//    val items = listOf("A", "B", "C", "D", "E", "F")
//    val disabledValue = "B"
//    var selectedIndex by remember { mutableStateOf(0) }
//    Box(modifier = Modifier.fillMaxSize().wrapContentSize(Alignment.TopStart)) {
//        Text(items[selectedIndex],modifier = Modifier.fillMaxWidth().clickable(onClick = { expanded = true }).background(
//            Color.Gray))
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false },
//            modifier = Modifier.fillMaxWidth().background(
//                Color.Red)
//        ) {
//            items.forEachIndexed { index, s ->
//                DropdownMenuItem(onClick = {
//                    selectedIndex = index
//                    expanded = false
//                },
//                    text = {stringos()}) {
//                    val disabledText = if (s == disabledValue) {
//                        " (Disabled)"
//                    } else {
//                        ""
//                    }
//                    Text(text = s + disabledText)
//                }
//            }
//        }
//    }
//}



@Preview
@Composable
fun SettingsScreenPreview() {
    CustomAppTheme {
        SettingsScreen()
    }
}
