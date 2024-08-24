package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.AppCustomTheme

class TopBarParameterProvider: PreviewParameterProvider<String> {
    override val values = sequenceOf(
        "Settings",
        "Camera",
        "Analytics"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarTitle(title: String) {

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            ) //todo jeste nejaky style apod?
        },

        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary //TODO nechat cernou nebo pouzivat tu primarni svetle modrou? (aby to pak ladilo s tou hlavni spodni navigaci)
        )
    )
}


@Preview
@Composable
fun TopBarTitlePreview(@PreviewParameter(TopBarParameterProvider::class) title: String) {
    AppCustomTheme {
        TopBarTitle(title)
    }
}
