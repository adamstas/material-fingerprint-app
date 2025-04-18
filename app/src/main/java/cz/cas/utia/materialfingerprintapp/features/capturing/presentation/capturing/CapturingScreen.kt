package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.core.ui.components.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.components.ForwardTopBarTitle

@Composable
fun CapturingScreenRoot(
    viewModel: CapturingViewModel = hiltViewModel(),
    navigateToPhotosSummaryScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CapturingEvent.LoadImages)
    }

    //observe navigation events and perform the navigation
    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                CapturingNavigationEvent.ToPhotosSummaryScreen -> navigateToPhotosSummaryScreen()
            }
        }
    )

    CapturingScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

//inspired by https://stackoverflow.com/questions/74780546/android-handle-lifecycle-event-on-jetpack-compose-screen
@Composable
fun CapturingScreen(
    state: CapturingScreenState,
    onEvent: (CapturingEvent) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val imageCapturedListener = remember {
        ImageCapturedListener { imageBitmap ->
            onEvent(CapturingEvent.CaptureImage(imageBitmap))
        }
    }

    val imageReadyToBeCapturedListener = remember {
        ImageReadyToBeCapturedListener { isReady ->
            onEvent(CapturingEvent.EnableOrDisableCaptureImageButton(isReady))
        }
    }

    val (parent, camView) = remember {
        val parent = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.camera, parent, true)

        val camView = parent.findViewById<CustomCameraView>(R.id.customCameraView).apply {
            setOnImageCapturedListener(imageCapturedListener)
            setImageReadyToBeCapturedListener(imageReadyToBeCapturedListener)
            setCameraPermissionGranted()
        }

        parent to camView
    }

    //camView's lifecycle is managed using DisposableEffect
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    camView.enableView()
                }
                Lifecycle.Event.ON_PAUSE -> {
                    camView.disableView()
                }
                else -> { }
            }
        }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
            camView.disableView()
        }
    }

    Scaffold(
        topBar = {
            ForwardTopBarTitle(
                title = "Capturing",
                navigateToNextScreen = { onEvent(CapturingEvent.GoToPhotosSummaryScreen) }
            )
        },
        content = { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(bottom = 16.dp)
            ) {
                TopImageBar(
                    state = state,
                    onEvent = onEvent
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { parent }
                    )
                }

               Button(
                   onClick = { camView.selectLastTargetPhoto() },
                   modifier = Modifier
                       .align(Alignment.CenterHorizontally)
                       .padding(top = 4.dp)
                       .padding(bottom = 4.dp),
                   enabled = state.isCaptureImageButtonEnabled
               ) {
                   Text("Capture")
               }

                if (state.isDialogShown) {
                    CapturedImageDialog(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }
        }
    )
}

@Composable
fun CapturedImageDialog(
    state: CapturingScreenState,
    onEvent: (CapturingEvent) -> Unit
) {
    Dialog(
        onDismissRequest = { onEvent(CapturingEvent.RetakeImage) }
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Captured Image",
                    style = MaterialTheme.typography.titleLarge
                )

                CustomSpacer()

                Image(
                    bitmap = state.currentlyCapturedImage!!.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier.aspectRatio(1f),
                    contentScale = ContentScale.Crop //fits the image inside the 1:1 ratio so it does not take just the middle of the image
                )

                CustomSpacer()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onEvent(CapturingEvent.RetakeImage) },
                        modifier = Modifier.weight(1f)) {
                            Text("Retake")
                    }

                    CustomSpacer()

                    Button(
                        onClick = { onEvent(CapturingEvent.KeepImage) },
                        modifier = Modifier.weight(1f)) {
                            Text("Keep")
                    }
                }
            }
        }
    }
}

@Composable
fun TopImageBar(
    state: CapturingScreenState,
    onEvent: (CapturingEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TopImageBarSlot(
            image = state.capturedImageSlot1,
            isSelected = state.isSlotSelected(ImageSlotPosition.FIRST),
            onClick = { onEvent(CapturingEvent.SelectImageSlot(ImageSlotPosition.FIRST)) }
        )

        TopImageBarSlot(
            image = state.capturedImageSlot2,
            isSelected = state.isSlotSelected(ImageSlotPosition.SECOND),
            onClick = { onEvent(CapturingEvent.SelectImageSlot(ImageSlotPosition.SECOND)) }
        )

        val text = if (state.isSlotSelected(ImageSlotPosition.FIRST))
            "Take a photo with light coming from the left side, at a 45° elevation."
        else "Take a photo with light from top/opposite side, at a 45° elevation. " +
                "The light reflection should appear in the center of the sample."

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun TopImageBarSlot(
    image: Bitmap?,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
    }

    val borderWidth = if (isSelected) {
        3.dp
    } else {
        1.dp
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .border(
                width = borderWidth,
                color = borderColor
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
        ) {
            if (image != null) {
                Image(
                    bitmap = image.asImageBitmap(),
                    contentDescription = "Captured image",
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.photo_camera),
                    contentDescription = "Empty slot",
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
    }
}
