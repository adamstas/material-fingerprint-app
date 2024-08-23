package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTitle(title: String) {

    CenterAlignedTopAppBar(
        title = {
            Text(text = title) //todo jeste nejaky style apod?
        }
    )
}