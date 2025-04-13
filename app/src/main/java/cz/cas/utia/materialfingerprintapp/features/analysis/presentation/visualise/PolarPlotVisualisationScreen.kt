package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.visualise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
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
import cz.cas.utia.materialfingerprintapp.core.ui.components.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.SingleChoiceSegmentedButton
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.commoncomponents.FindSimilarMaterialsDialog
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.commoncomponents.NonInteractivePolarPlot
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.commoncomponents.PolarPlotLegendRow

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
                PolarPlotVisualisationNavigationEvent.Back -> navigateBack()
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
                navigateBack = { onEvent(PolarPlotVisualisationEvent.GoBack) }
            )
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                val availableHeight = maxHeight

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
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

                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false) // stretch to rest of the screen
                            // but if the content is already at maximum of its size determined inside the Box then do not stretch
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            when (state.plotDisplayMode) {
                                PlotDisplayMode.SINGLE_PLOT -> {
                                    NonInteractivePolarPlot(
                                        firstAxisValues = state.axisValuesFirst,
                                        axisLabels = axisLabels,
                                        secondAxisValues = state.axisValuesSecond,
                                        firstPlotColor = primaryPlotColor,
                                        secondPlotColor = secondaryPlotColor,
                                        maxPlotSize = (availableHeight * 0.6f).coerceAtMost(400.dp)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    ) {
                                        Button(
                                            onClick = { onEvent(PolarPlotVisualisationEvent.FindSimilarMaterial) },
                                            enabled = state.areBottomButtonsEnabled(),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = "Find similar materials")
                                        }

                                        Button(
                                            onClick = { onEvent(PolarPlotVisualisationEvent.ApplyFilter) },
                                            enabled = state.areBottomButtonsEnabled(),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = "Apply filter")
                                        }
                                    }
                                }

                                PlotDisplayMode.TWO_PLOTS -> {
                                    val plotSize = (availableHeight * 0.45f).coerceAtMost(300.dp)

                                    Box(
                                        modifier = Modifier
                                            .size(plotSize)
                                            .aspectRatio(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        NonInteractivePolarPlot(
                                            firstAxisValues = state.axisValuesFirst,
                                            axisLabels = axisLabels,
                                            firstPlotColor = primaryPlotColor,
                                            secondPlotColor = secondaryPlotColor,
                                            maxPlotSize = plotSize
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(plotSize)
                                            .aspectRatio(1f),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        NonInteractivePolarPlot(
                                            firstAxisValues = state.axisValuesSecond!!,
                                            axisLabels = axisLabels,
                                            firstPlotColor = secondaryPlotColor,
                                            secondPlotColor = secondaryPlotColor,
                                            maxPlotSize = plotSize
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(bottom = 24.dp)
                            .height(IntrinsicSize.Min) // set height to the minimum needed
                    // to properly display the content of this column
                    ) {
                        Text(text = "Plot amount:")

                        Spacer(modifier = Modifier.height(8.dp))

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
                    }
                }
            }

            if (state.isFindSimilarMaterialsDialogShown) {
                FindSimilarMaterialsDialog(
                    onDismissRequest = { onEvent(PolarPlotVisualisationEvent.DismissFindSimilarMaterialsDialog) },
                    onLocalDatabaseButtonClick = { onEvent(PolarPlotVisualisationEvent.FindSimilarLocalMaterial) },
                    onRemoteDatabaseButtonClick = { onEvent(PolarPlotVisualisationEvent.FindSimilarRemoteMaterial) }
                )
            }
        }
    )
}