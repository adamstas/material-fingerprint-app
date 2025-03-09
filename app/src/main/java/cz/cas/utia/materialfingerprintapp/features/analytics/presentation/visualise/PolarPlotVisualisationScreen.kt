package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.visualise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.SingleChoiceSegmentedButton
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents.FindSimilarMaterialsDialog
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents.NonInteractivePolarPlot
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents.PolarPlotLegendRow

@Composable
fun PolarPlotVisualisationScreenRoot(
    viewModel: PolarPlotVisualisationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToApplyFilterScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                PolarPlotVisualisationNavigationEvent.BackToBrowseMaterialsScreen -> navigateBack()
                PolarPlotVisualisationNavigationEvent.ToApplyFilterScreen -> navigateToApplyFilterScreen()
                is PolarPlotVisualisationNavigationEvent.ToBrowseSimilarLocalMaterialsScreen -> navigateToBrowseSimilarLocalMaterialsScreen(event.materialId)
                is PolarPlotVisualisationNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen -> navigateToBrowseSimilarRemoteMaterialsScreen(event.materialId)
            }
        }
    )

    PolarPlotVisualisationScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun PolarPlotVisualisationScreen(
    state: PolarPlotVisualisationScreenState,
    onEvent: (PolarPlotVisualisationEvent) -> Unit
) {
    val primaryPlotColor = colorResource(id = AppConfig.Colors.primaryPlotColorId)
    val secondaryPlotColor = colorResource(id = AppConfig.Colors.secondaryPlotColorId)

    Scaffold(
        topBar = {
            BackTopBarTitle(
                title = "Polar plot visualisation",
                navigateBack = { onEvent(PolarPlotVisualisationEvent.GoBackToBrowseMaterialsScreen) }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        PolarPlotLegendRow(
                            rectangleColor = primaryPlotColor,
                            text = state.firstMaterialName
                        )

                        if (state.axisValuesSecond != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                            PolarPlotLegendRow(
                                rectangleColor = secondaryPlotColor,
                                text = state.secondMaterialName!!
                            )
                        }
                    }

                    IconButton(onClick = { onEvent(PolarPlotVisualisationEvent.ShowOrHideAxesLabels) }) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_dialog_info),
                            contentDescription = "Show axes labels"
                        )
                    }
                }

                val axisLabels = AppConfig.PolarPlot.axisLabels.takeIf { state.showAxisLabels }

                when (state.plotDisplayMode) {
                    PlotDisplayMode.SINGLE_PLOT -> {
                        NonInteractivePolarPlot(
                            firstAxisValues = state.axisValuesFirst,
                            axisLabels = axisLabels,
                            secondAxisValues = state.axisValuesSecond,
                            firstPlotColor = primaryPlotColor,
                            secondPlotColor = secondaryPlotColor
                        )

                        Spacer(modifier = Modifier.height(24.dp))


                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = { onEvent(PolarPlotVisualisationEvent.FindSimilarMaterial) },
                                enabled = state.areBottomButtonsEnabled()
                            ) {
                                Text(text = "Find similar materials")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { onEvent(PolarPlotVisualisationEvent.ApplyFilter) },
                                enabled = state.areBottomButtonsEnabled()
                            ) {
                                Text(text = "Apply filter")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp)) // todo celkove na teto obrazovce ty Spacery udelat mensi (a konzistentni!) a resit sizovani tech plotů pomoci weight()
                    }

                    PlotDisplayMode.TWO_PLOTS -> {
                        NonInteractivePolarPlot(
                            firstAxisValues = state.axisValuesFirst,
                            axisLabels = axisLabels,
                            firstPlotColor = primaryPlotColor,
                            secondPlotColor = secondaryPlotColor,
                            maxPlotSize = 250.dp,
                        )
                        NonInteractivePolarPlot(
                            firstAxisValues = state.axisValuesSecond!!,
                            axisLabels = axisLabels,
                            firstPlotColor = secondaryPlotColor,
                            secondPlotColor = secondaryPlotColor,
                            maxPlotSize = 250.dp,
                        )
                    }
                }

                Text(text = "Plot amount:")

                SingleChoiceSegmentedButton(
                    selectedOption = state.plotDisplayMode,
                    options = PlotDisplayMode.entries,
                    onSelectionChange = { newMode ->
                        onEvent(PolarPlotVisualisationEvent.SetPlotDisplayMode(newMode))
                    },
                    enabled = state.isSegmentedButtonEnabled(),
                    label = { mode ->
                        when (mode) {
                            PlotDisplayMode.SINGLE_PLOT -> "Single"
                            PlotDisplayMode.TWO_PLOTS -> "Two"
                        }
                    }
                )

                if (state.isFindSimilarMaterialsDialogShown) {
                    FindSimilarMaterialsDialog(
                        onDismissRequest = { onEvent(PolarPlotVisualisationEvent.DismissFindSimilarMaterialsDialog) },
                        onLocalDatabaseButtonClick = { onEvent(PolarPlotVisualisationEvent.FindSimilarLocalMaterial) },
                        onRemoteDatabaseButtonClick = { onEvent(PolarPlotVisualisationEvent.FindSimilarRemoteMaterial) }
                    )
                }
            }
        })
}