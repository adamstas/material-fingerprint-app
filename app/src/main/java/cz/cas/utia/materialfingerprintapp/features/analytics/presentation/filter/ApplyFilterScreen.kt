package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents.PolarPlotCanvas
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sign
import kotlin.math.sin

@Composable
fun ApplyFilterScreenRoot(
    viewModel: ApplyFilterViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    navigateToBrowseSimilarLocalMaterialsScreen: () -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                ApplyFilterNavigationEvent.BackToAnalyticsHomeScreen -> navigateBack()
                ApplyFilterNavigationEvent.ToBrowseSimilarLocalMaterialsScreen -> navigateToBrowseSimilarLocalMaterialsScreen()
                ApplyFilterNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen -> navigateToBrowseSimilarRemoteMaterialsScreen()
            }
        }
    )

    ApplyFilterScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun ApplyFilterScreen(
    state: ApplyFilterScreenState,
    onEvent: (ApplyFilterEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBarTitle(
                title = "Apply filter",
                navigateBack = { onEvent(ApplyFilterEvent.GoBackToAnalyticsHomeScreen) }
            )
        }, // todo udelat do columnu at neni tolik mista nahore mezi ikonkou icka a polar plotem? viz obrazovka PolarPlotVisualisationScreen
        content = { paddingValues ->
            Box( // so the content can be horizontally centered
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    //.windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Top
                ) {
                    IconButton(onClick = { onEvent(ApplyFilterEvent.ShowOrHideAxesLabels) }) {
                        Icon(
                            painter = painterResource(android.R.drawable.ic_dialog_info),
                            contentDescription = "Show axes names"
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onEvent(ApplyFilterEvent.UndoDrawingState) },
                        enabled = state.isUndoButtonEnabled()
                    ) {
                        Text("Undo")
                    }
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CustomSpacer()

                    PolarPlotWithSliders(
                        state = state,
                        onEvent = onEvent
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = { onEvent(ApplyFilterEvent.ApplyOnLocalData) }) {
                            Text(text = "Apply on local data")
                        }

                        Button(
                            onClick = { onEvent(ApplyFilterEvent.ApplyOnServerData) }) {
                            Text(text = "Apply on server data")
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun PolarPlotWithSliders(
    state: ApplyFilterScreenState,
    onEvent: (ApplyFilterEvent) -> Unit
) {
    val axesAmount = state.axisValues.size

    val circleColor = MaterialTheme.colorScheme.primary // for drawing colors, cannot be obtained later
    val backgroundColor = MaterialTheme.colorScheme.background
    val plotColor = colorResource(id = AppConfig.Colors.primaryPlotColorId)

    // these attributes are changed in each frame when dragging so they are transient state and not needed to be stored at viewmodel
    var activeAxis by remember { mutableStateOf<Int?>(null) }
    var originalXSign by remember { mutableStateOf(0f) }
    var originalYSign by remember { mutableStateOf(0f) }
    val lockedAtCenter = remember { BooleanArray(axesAmount) { false } } // this does not need to trigger recomposition so no "by" is needed

    // to recompose the pointerInput modifier so it correctly reacts on new axisValues from state
    var recomposePointerInput by remember { mutableStateOf(false) }

    val canvasSizeState = remember { mutableStateOf(Size.Zero) }
    val canvasSize = canvasSizeState.value

    // when value changes the pointer input is recomposed and here value is set back to false
    LaunchedEffect(recomposePointerInput) {
        recomposePointerInput = false
    }

    val newAxisValues = state.axisValues.toMutableList()

    val canvasModifier = Modifier
        .fillMaxSize()
        .background(color = backgroundColor)

    val pointerInputModifier = canvasModifier.pointerInput(recomposePointerInput) {
        detectDragGestures(
            // when user starts dragging determine if some point is within his finger radius
            onDragStart = { offset ->
                val size = canvasSize
                val center = Offset(size.width / 2f, size.height / 2f) // circle center
                val sensitivity = 65f // the bigger the value is the bigger the radius around points is (but if too big user can select another point accidentally)
                var closestDistance = Float.MAX_VALUE

                for (i in 0 until axesAmount) {
                    val angle = (2 * PI * i / axesAmount).toFloat() // axis angle
                    val pointX = center.x + cos(angle) * state.axisValues[i] * (size.width / 2 / 300f) // pointX is the x coord of the point in the polar plot; scale to our coords system
                    val pointY = center.y + sin(angle) * state.axisValues[i] * (size.width / 2 / 300f)

                    val distance = hypot(offset.x - pointX, offset.y - pointY)

                    if (distance < sensitivity && distance < closestDistance) { // check if this point is the nearest one
                        activeAxis = i
                        closestDistance = distance
                        originalXSign = sign(offset.x - center.x) // remember the point’s coords’ sign due to prevent bug when user drags the point in direction to the center and the point moves the other way
                        originalYSign = sign(offset.y - center.y)
                    }
                }
            },
            onDrag = { change, _ ->
                change.consume()
                activeAxis?.let { axis -> // if no axis point was clicked then nothing happens during drag
                    val size = canvasSize
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val maxRadius = size.width / 2f // maximally from the center of the polar plot to the outer circle

                    val distanceX = change.position.x - center.x // how far from center is the point after drag
                    val distanceY = change.position.y - center.y

                    if (sign(distanceX) != originalXSign && sign(distanceY) != originalYSign) { // if user dragged to the opposite quadrant of the circle
                        //onAxisValueChange(axis, 0f)
                        onEvent(
                            ApplyFilterEvent.SetAxisValue(
                                axisId = axis,
                                value = 0f
                            )
                        )
                        newAxisValues[axis] = 0f
                        lockedAtCenter[axis] = true
                    } else if (lockedAtCenter[axis] && (sign(distanceX) == originalXSign || sign(distanceY) == originalYSign)) {
                        lockedAtCenter[axis] = false // unlock the axis if user goes the right direction
                    }

                    var newDistanceValue = state.axisValues[axis]
                    if (!lockedAtCenter[axis]) { // if not locked move the point
                        newDistanceValue = (hypot(distanceX, distanceY) / maxRadius * 300f).coerceIn(0f, 300f)
                        //onAxisValueChange(axis, newDistanceValue)
                        onEvent(
                            ApplyFilterEvent.SetAxisValue(
                                axisId = axis,
                                value = newDistanceValue
                            )
                        )
                        newAxisValues[axis] = newDistanceValue
                    }
                    onEvent(ApplyFilterEvent.SetSelectedAxisValue(newDistanceValue))
                }
            },
            onDragEnd = { // push current state to stack only after drag completion with some active axis (no dragging without points)
                activeAxis?.let { axis ->
                    onEvent(ApplyFilterEvent.AddDrawingStateToStack(newAxisValues)) // if some drag was performed add it to the stack
                    lockedAtCenter[axis] = false // unlock the active axis
                    activeAxis = null
                    recomposePointerInput = true // recompose the pointer input because dragging was successful
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // just to set max size of the polar plot so it does not take whole screen on tablets // todo takhle to nefunguje..
        Box(
            modifier = Modifier
                .sizeIn(
                    maxWidth = 400.dp,
                    maxHeight = 400.dp
                )
                .aspectRatio(1f)
        ) {

            val axisLabels = AppConfig.PolarPlot.axisLabels.takeIf { state.showAxisLabels }

            PolarPlotCanvas(
                axisValues = state.axisValues,
                axisLabels = axisLabels,
                circleColor = circleColor,
                backgroundColor = backgroundColor,
                firstPlotColor = plotColor,
                isInteractive = true,
                activeAxis = activeAxis,
                pointRadius = 15f,
                canvasSizeState = canvasSizeState,
                modifier = pointerInputModifier
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Value: ${"%.2f".format(scaleToCharacteristics(state.selectedAxisValue))}",
            style = MaterialTheme.typography.bodyLarge,
            color = circleColor,
            textAlign = TextAlign.Center
        )
    }
}


