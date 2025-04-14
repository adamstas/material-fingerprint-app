package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SLOT1_IMAGE_NAME_WITH_SUFFIX
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SLOT2_IMAGE_NAME_WITH_SUFFIX
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
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
class CapturingViewModel @Inject constructor(
    private val imageStorageService: ImageStorageService
): ViewModel()
{
    private val _state = MutableStateFlow(CapturingScreenState())
    val state = _state.asStateFlow()

    //for navigation events
    private val _navigationEvents = MutableSharedFlow<CapturingNavigationEvent>()
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
            ImageSlotPosition.FIRST -> imageStorageService.storeImage(image = image, filename = SLOT1_IMAGE_NAME_WITH_SUFFIX)
            ImageSlotPosition.SECOND -> imageStorageService.storeImage(image = image, filename = SLOT2_IMAGE_NAME_WITH_SUFFIX)
        }
    }

    fun onEvent(event: CapturingEvent) {
        when (event) {
            is CapturingEvent.CaptureImage -> captureImage(event)
            CapturingEvent.RetakeImage -> retakeImage()
            is CapturingEvent.SelectImageSlot -> selectImageSlot(event)
            CapturingEvent.KeepImage -> keepImage()
            is CapturingEvent.EnableOrDisableCaptureImageButton -> enableOrDisableCaptureImageButton(event)
            CapturingEvent.GoToPhotosSummaryScreen -> goToPhotosSummaryScreen()
            CapturingEvent.LoadImages -> loadImages()
        }
    }

    private fun captureImage(event: CapturingEvent.CaptureImage) {
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

    private fun selectImageSlot(event: CapturingEvent.SelectImageSlot) {
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

    private fun enableOrDisableCaptureImageButton(event: CapturingEvent.EnableOrDisableCaptureImageButton) {
        _state.update {
            it.copy(
                isCaptureImageButtonEnabled = event.enable
            )
        }
    }

    private fun goToPhotosSummaryScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(CapturingNavigationEvent.ToPhotosSummaryScreen)
        }
    }

    private fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageSlot1 = imageStorageService.loadImage(SLOT1_IMAGE_NAME_WITH_SUFFIX)
            val imageSlot2 = imageStorageService.loadImage(SLOT2_IMAGE_NAME_WITH_SUFFIX)

            withContext(Dispatchers.Main) {
                _state.update { it.copy(
                    capturedImageSlot1 = imageSlot1,
                    capturedImageSlot2 = imageSlot2
                ) }
            }
        }
    }
}