package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackTopBarTitle(
    title: String,
    navigateBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navigateBack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack, //todo udelat ji nejakou barvou z Theme?
                    contentDescription = "Go back" //todo zmenit?
                )
            }
        },

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