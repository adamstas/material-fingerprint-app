package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.IMAGE_SUFFIX
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    //todo add service for calling server API for uploading images
    private val imageStorageService: ImageStorageService
): ViewModel()
{
    //todo typ "Bitmap?" jsem chtel obalit do State ale byl by to jen empty/available takze budu prote rovnou hceckovat jestli image je nebo neni null

    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state.asStateFlow()

    //for navigation events
    private val _navigationEvents = MutableSharedFlow<CameraNavigationEvent>()
    //todo if there are some navigation bugs when app is in the background then add replay = 10 or something (https://www.youtube.com/watch?v=BFhVvAzC52w&ab_channel=PhilippLackner 8:00)
    val navigationEvents = _navigationEvents.asSharedFlow()

    //always select the next empty slot and if no other empty slot exists return the current one
    private fun getNewSelectedImageSlotPosition(): ImageSlotPosition {
        return when (_state.value.selectedImageSlot) {
            ImageSlotPosition.FIRST -> {
                if (_state.value.capturedImageSlot2 == null)
                    ImageSlotPosition.SECOND
                else
                    ImageSlotPosition.FIRST
            }

            ImageSlotPosition.SECOND -> {
                if (_state.value.capturedImageSlot1 == null)
                    ImageSlotPosition.FIRST
                else
                    ImageSlotPosition.SECOND
            }
        }
    }

    private fun storeImage(slot: ImageSlotPosition, image: Bitmap) {
        when (slot) {
            ImageSlotPosition.FIRST -> imageStorageService.storeImage(image = image, filename = "slot1$IMAGE_SUFFIX")
            ImageSlotPosition.SECOND -> imageStorageService.storeImage(image = image, filename = "slot2$IMAGE_SUFFIX")
        }
    }

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.CaptureImage -> captureImage(event)
            CameraEvent.RetakeImage -> retakeImage()
            is CameraEvent.SelectImageSlot -> selectImageSlot(event)
            CameraEvent.KeepImage -> keepImage()
            is CameraEvent.EnableOrDisableCaptureImageButton -> enableOrDisableCaptureImageButton(event)
            CameraEvent.GoToPhotosSummaryScreen -> goToPhotosSummaryScreen()
            CameraEvent.LoadImages -> loadImages()
        }
    }

    private fun captureImage(event: CameraEvent.CaptureImage) {
        _state.update {
            it.copy(
                currentlyCapturedImage = event.imageBitmap,
                isDialogShown = true
            )
        }
    }

    private fun retakeImage() {
        _state.update {
            it.copy(
                currentlyCapturedImage = null,
                isDialogShown = false
            )
        }
    }

    private fun selectImageSlot(event: CameraEvent.SelectImageSlot) {
        _state.update {
            it.copy(
                selectedImageSlot = event.slotPosition
            )
        }
    }

    private fun keepImage() {
        _state.update {
            val updated = when (_state.value.selectedImageSlot) {
                ImageSlotPosition.FIRST -> it.copy(capturedImageSlot1 = _state.value.currentlyCapturedImage)
                ImageSlotPosition.SECOND -> it.copy(capturedImageSlot2 = _state.value.currentlyCapturedImage)
            }

            storeImage(slot = _state.value.selectedImageSlot, _state.value.currentlyCapturedImage!!)

            updated.copy(
                currentlyCapturedImage = null,
                selectedImageSlot = getNewSelectedImageSlotPosition(), //automatically go to next empty slot (if any exists)
                isDialogShown = false
            )
        }
    }

    private fun enableOrDisableCaptureImageButton(event: CameraEvent.EnableOrDisableCaptureImageButton) {
        _state.update {
            it.copy(
                isCaptureImageButtonEnabled = event.enable
            )
        }
    }

    private fun goToPhotosSummaryScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(CameraNavigationEvent.ToPhotosSummaryScreen)
        }
    }

    private fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageSlot1 = imageStorageService.loadImage("slot1$IMAGE_SUFFIX")
            val imageSlot2 = imageStorageService.loadImage("slot2$IMAGE_SUFFIX")

            withContext(Dispatchers.Main) {
                _state.update { it.copy(
                    capturedImageSlot1 = imageSlot1,
                    capturedImageSlot2 = imageSlot2
                ) }
            }
        }
    }
}