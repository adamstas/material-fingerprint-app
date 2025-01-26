package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.BasicDropdownMenu
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

@Composable
fun PhotosSummaryScreenRoot(
    viewModel: PhotosSummaryViewModel = hiltViewModel(),
    navigateBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    //during each recomposition reload the images
    LaunchedEffect(Unit) {
        viewModel.onEvent(PhotosSummaryEvent.LoadImages)
    }

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
                    .padding(24.dp)
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MaterialNameRow(state, onEvent)

                CustomSpacer()

                CategorySelectionRow(state, onEvent)

                CustomSpacer()
                CustomSpacer()

                CenteredImage(
                    image = state.capturedImageSlot1,
                    lightDirection = state.lightDirectionSlot1,
                    onArrowIconClick = { onEvent(PhotosSummaryEvent.SwitchLightDirections) })

                CustomSpacer()

                CenteredImage(
                    image = state.capturedImageSlot2,
                    lightDirection = state.lightDirectionSlot2,
                    onArrowIconClick = { onEvent(PhotosSummaryEvent.SwitchLightDirections) })

                CustomSpacer()

                AnalyseButton(state, onEvent)
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Material name:")

        Spacer(modifier = Modifier.size(8.dp))

        TextField(
            value = state.materialName,
            onValueChange = { name: String ->
                onEvent(PhotosSummaryEvent.SetName(name))
            },
            placeholder = { Text(text = "Enter material name") },
            singleLine = true //todo testnout a dat i jinam?
        )
    }
}

@Composable
fun CategorySelectionRow(
    state: PhotosSummaryScreenState,
    onEvent: (PhotosSummaryEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Select category:")

        Spacer(modifier = Modifier.size(8.dp))

        CategoriesDropdownMenu(state, onEvent)
    }
}

@Composable
fun CategoriesDropdownMenu(
    state: PhotosSummaryScreenState,
    onEvent: (PhotosSummaryEvent) -> Unit
) {
    BasicDropdownMenu(
        expanded = state.isDropdownMenuExpanded,
        onDropdownMenuClick = { onEvent(PhotosSummaryEvent.ShowDropdownMenu) },
        onDropdownMenuClosed = { onEvent(PhotosSummaryEvent.CloseDropdownMenu) },
        onDropdownMenuItemClick = { category: MaterialCategory ->
            onEvent(PhotosSummaryEvent.SelectCategory(category)) },
        options = MaterialCategory.entries,
        selectedOption = state.selectedCategory,
        modifier = Modifier.width(215.dp) //todo keep fixed?
    )
}

@Composable
fun CenteredImage(
    image: Bitmap?,
    lightDirection: LightDirection,
    onArrowIconClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageAndIconSize = 200.dp

        if (image != null) {
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "Captured image",
                modifier = Modifier
                    .size(imageAndIconSize)
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                painter = painterResource(R.drawable.photo_camera),
                contentDescription = "Empty slot",
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier
                    .size(imageAndIconSize)
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.height(imageAndIconSize),
            verticalArrangement = Arrangement.Bottom
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sun),
                contentDescription = "Sun icon",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onArrowIconClick() }
            )

            val arrowIconResource = when (lightDirection) {
                LightDirection.FROM_LEFT -> R.drawable.arrow_forward
                LightDirection.FROM_ABOVE -> R.drawable.arrow_down
            }

            Icon(
                painter = painterResource(id = arrowIconResource),
                contentDescription = "Arrow Icon",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onArrowIconClick() }
            )
        }
    }
}

@Composable
fun AnalyseButton(
    state: PhotosSummaryScreenState,
    onEvent: (PhotosSummaryEvent) -> Unit
) {
    Button(
        enabled = state.isReadyToAnalyse(),
        onClick = { onEvent(PhotosSummaryEvent.AnalyseImages) }
    ) {
        Text(text = "Analyse")
    }
}