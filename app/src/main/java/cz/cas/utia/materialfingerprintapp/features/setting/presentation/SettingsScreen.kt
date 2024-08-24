package cz.cas.utia.materialfingerprintapp.features.setting.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle

@Preview(showBackground = true)
@Composable
fun SettingsScreen(
//    bulyn: Boolean,
//    bulyn2: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(true) } //just for temporary switch
        Scaffold(
        topBar = {
            TopBarTitle(title = "Settings")
            }
        )

    { paddingValues -> //todo proc padding nefunguje? Je to OK, bez toho by to bylo prekryte tou horni listou
        Column(
            Modifier
                .padding(paddingValues) //todo jeste padding ze stran, ale nevim jestli fixni nebo ne
               //.fillMaxSize() //todo ma to nejaky vliv?
        )
        {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                //verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Send data to the server (anonymously)")
                Switch( checked = checked, onCheckedChange = {
                    checked = it
                })
            }
        }
    }
}
