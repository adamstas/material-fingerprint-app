package cz.cas.utia.materialfingerprintapp.features.setting.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle

@Composable
fun PermissionsScreen() {
    Scaffold(
        topBar = {
            TopBarTitle(title = "Permissions")
        }
    ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(25.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Please provide following permissions for the app to work on your device:") //todo mozna vetsi pismo nebo nejak tucne..

                CustomHorizontalDivider()

                permissions.forEach {
                    Permission(permission = it)
                    CustomHorizontalDivider()
                }
            }
    }
}

@Composable
fun Permission(permission: Permissions) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = permission.title,
            style = MaterialTheme.typography.titleMedium)

        Icon(
            painter = painterResource(permission.iconId),
            contentDescription = permission.title //todo zmenit na nejaky sofistikovanejsi popis?
        )
    }
    CustomSpacer()
    
    Text(text = permission.description)

    CustomSpacer()
    
    Button(
        onClick = { /*TODO*/ }
    ) {
      Text(text = "Grant permission") //todo or "Request permission/Ask for permission"?
    }
}