package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle

@Composable
fun AnalyticsHomeScreenRoot(
    viewModel: AnalyticsHomeViewModel = hiltViewModel(),
    navigateToBrowseLocalMaterialsScreen: () -> Unit,
    navigateToBrowseRemoteMaterialsScreen: () -> Unit,
    navigateToApplyFilterScreen: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                AnalyticsHomeNavigationEvent.ToApplyFilterScreen -> navigateToApplyFilterScreen()
                AnalyticsHomeNavigationEvent.ToBrowseLocalMaterialsScreen -> navigateToBrowseLocalMaterialsScreen()
                AnalyticsHomeNavigationEvent.ToBrowseRemoteMaterialsScreen -> navigateToBrowseRemoteMaterialsScreen()
            }
        }
    )

    AnalyticsHomeScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun AnalyticsHomeScreen(
    state: AnalyticsHomeScreenState, //todo if not used later, can be removed (the whole object definition of AnalyticsHomeScreenState)
    onEvent: (AnalyticsHomeEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopBarTitle(
                title = "Analytics"
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Browse materials",
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Button(
                            onClick = { onEvent(AnalyticsHomeEvent.BrowseLocalMaterials) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Browse local materials")
                        }
                        Button(
                            onClick = { onEvent(AnalyticsHomeEvent.BrowseRemoteMaterials) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Browse remote materials")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Filter materials",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Button(
                        onClick = { onEvent(AnalyticsHomeEvent.SearchForMaterialsBasedOnTheirFingerprint) },
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Search for materials based on their fingerprint")
                    }
                }
            }
        }
    )
}