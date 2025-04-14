package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing

import android.graphics.Bitmap

sealed interface CapturingEvent {
    data class CaptureImage(val imageBitmap: Bitmap): CapturingEvent
    data object RetakeImage: CapturingEvent
    data class SelectImageSlot(val slotPosition: ImageSlotPosition): CapturingEvent
    data object KeepImage: CapturingEvent

    data class EnableOrDisableCaptureImageButton(val enable: Boolean): CapturingEvent

    data object GoToPhotosSummaryScreen: CapturingEvent

    data object LoadImages: CapturingEvent
}

sealed interface CapturingNavigationEvent {
    data object ToPhotosSummaryScreen: CapturingNavigationEvent
}