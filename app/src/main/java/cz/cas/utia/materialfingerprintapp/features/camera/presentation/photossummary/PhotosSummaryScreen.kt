package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler

import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle

@Composable
fun PhotosSummaryScreenRoot(
    viewModel: PhotosSummaryViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                PhotosSummaryNavigationEvent.BackToCameraScreen -> navigateBack()
            }
        }
    )

    PhotosSummaryScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun PhotosSummaryScreen(
    state: PhotosSummaryScreenState,
    onEvent: (PhotosSummaryEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBarTitle(
                title = "Photos summary",
                navigateBack = { onEvent(PhotosSummaryEvent.GoBackToCameraScreen) }
            )
        },
        content = { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                   // .padding(bottom = 16.dp) todo zase doladit padding az to bude..
            ) {
                MaterialNameRow(state, onEvent)

            }
        }
    )
}

@Composable
fun MaterialNameRow(
    state: PhotosSummaryScreenState,
    onEvent: (PhotosSummaryEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Material name:")

        TextField(
            value = state.materialName,
            onValueChange = { name: String ->
                onEvent(PhotosSummaryEvent.SetName(name))
            },
            placeholder = { Text(text = "Enter material name") },
           // modifier = modifier
        )
    }
}