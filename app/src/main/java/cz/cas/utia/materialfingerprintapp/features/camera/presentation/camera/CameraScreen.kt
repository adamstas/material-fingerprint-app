package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import cz.cas.utia.materialfingerprintapp.CustomCameraView
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.components.ForwardTopBarTitle

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreenRoot(
    viewModel: CameraViewModel = hiltViewModel(),
    navigateToPhotosSummaryScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    //todo nebuguje to pak s temi permissions?
    //observe navigation events and perform the navigation
    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                CameraNavigationEvent.ToPhotosSummaryScreen -> navigateToPhotosSummaryScreen()
            }
        }
    )

    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    //todo later move to permissions screen
    if (cameraPermissionState.status.isGranted) {
        CameraScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    } else {
        LaunchedEffect(Unit) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}

//inspired by https://stackoverflow.com/questions/74780546/android-handle-lifecycle-event-on-jetpack-compose-screen
@Composable
fun CameraScreen(
    state: CameraScreenState,
    onEvent: (CameraEvent) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    //todo nechat pres remember?
    val imageCapturedListener = remember {
        ImageCapturedListener { imageBitmap ->
            onEvent(CameraEvent.CaptureImage(imageBitmap))
        }
    }

    //todo nechat pres remember?
    val imageReadyToBeCapturedListener = remember {
        ImageReadyToBeCapturedListener { isReady ->
            onEvent(CameraEvent.EnableOrDisableCaptureImageButton(isReady))
        }
    }

    //todo nechat pres remember?
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
            setCameraPermissionGranted() //TODO toto tu bude i pozdeji az budu nastavovat permissions separatne? asi jo..
        }

        parent to camView
    }

    //todo rozhodnout se zda bude v topbaru kromě šipky doprava na dalsi screen (Photo Summary) i přehled 2 fotek anebo jestli to bude v liště hned pod top barem (nejdriv to asi zkusit narvat nahoru protoze tam nebude ani nazev screeny)

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
                title = "Camera",
                navigateToNextScreen = { onEvent(CameraEvent.GoToPhotosSummaryScreen) }
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

                if (state.isDialogOpened) {
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
    state: CameraScreenState,
    onEvent: (CameraEvent) -> Unit
) {
    Dialog(
        onDismissRequest = { onEvent(CameraEvent.RetakeImage) }
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
                    text = "Captured Image", //todo add number of slot to which the image was taken?
                    style = MaterialTheme.typography.titleLarge
                )

                CustomSpacer()

                state.currentlyCapturedImage?.let { //needed to do this "let" because state.currentlyCapturedImage is nullable
                    Image( //todo check if this image looks the same as the image that will be sent to server
                        bitmap = it.asImageBitmap(),//todo if issues with images this may be a problem (description of the asImageBitmap method)
                        contentDescription = "Captured image",
                        modifier = Modifier.aspectRatio(1f),
                        contentScale = ContentScale.Crop //fits the image inside the 1:1 ratio so it does not take just the middle of the image
                    )
                }

                CustomSpacer()

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { onEvent(CameraEvent.RetakeImage) },
                        modifier = Modifier.weight(1f)) {
                            Text("Retake")
                    }

                    CustomSpacer()

                    Button(
                        onClick = { onEvent(CameraEvent.KeepImage) },
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
    state: CameraScreenState,
    onEvent: (CameraEvent) -> Unit
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
            onClick = { onEvent(CameraEvent.SelectImageSlot(ImageSlotPosition.FIRST)) }
        )

        TopImageBarSlot(
            image = state.capturedImageSlot2,
            isSelected = state.isSlotSelected(ImageSlotPosition.SECOND),
            onClick = { onEvent(CameraEvent.SelectImageSlot(ImageSlotPosition.SECOND)) }
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
                    contentDescription = "Captured image", //todo psat sem i slot?
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
