package cz.cas.utia.materialfingerprintapp.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForwardTopBarTitle(
    title: String,
    navigateToNextScreen: () -> Unit
) {
    CenterAlignedTopAppBar(
        actions = {
            IconButton(onClick = {
                navigateToNextScreen()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go forward"
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