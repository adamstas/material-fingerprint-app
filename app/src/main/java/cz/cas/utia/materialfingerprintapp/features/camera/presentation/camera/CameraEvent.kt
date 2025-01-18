package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

import android.graphics.Bitmap

//todo keep naming convention so this is CameraEvent and in material screen there is MaterialEvent?
sealed interface CameraEvent {
    data class CaptureImage(val imageBitmap: Bitmap): CameraEvent
    data object RetakeImage: CameraEvent
    data class SelectImageSlot(val slotPosition: ImageSlotPosition): CameraEvent
    data object KeepImage: CameraEvent

    data class EnableOrDisableCaptureImageButton(val enable: Boolean): CameraEvent
}