package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    //todo add service for calling server API for uploading images
): ViewModel()
{
    //todo typ "Bitmap?" jsem chtel obalit do State ale byl by to jen empty/available takze budu prote rovnou hceckovat jestli image je nebo neni null

    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state.asStateFlow()

    // always select the next empty slot and if no other empty slot exists return the current one
    private fun getNewSelectedImageSlotPosition(): ImageSlotPosition{
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

    fun onEvent(event: CameraEvent) {
        when (event) {
            is CameraEvent.CaptureImage -> captureImage(event)
            CameraEvent.RetakeImage -> retakeImage()
            is CameraEvent.SelectImageSlot -> selectImageSlot(event)
            CameraEvent.KeepImage -> keepImage()
            is CameraEvent.EnableOrDisableCaptureImageButton -> enableOrDisableCaptureImageButton(event)
        }
    }

    private fun captureImage(event: CameraEvent.CaptureImage) {
        Log.i("CAMERATAGOS", "image captured in logs")

        _state.update {
            it.copy(
                currentlyCapturedImage = event.imageBitmap,
                isDialogOpened = true
            )
        }
    }

    private fun retakeImage() {
        _state.update {
            it.copy(
                currentlyCapturedImage = null,
                isDialogOpened = false
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

            updated.copy(
                currentlyCapturedImage = null,
                selectedImageSlot = getNewSelectedImageSlotPosition(), //automatically go to next empty slot (if any exists)
                isDialogOpened = false
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
}