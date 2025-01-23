package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

sealed interface PhotosSummaryEvent {
    /**
     *  data class CaptureImage(val imageBitmap: Bitmap): CameraEvent
     *     data object RetakeImage: CameraEvent
     *     data class SelectImageSlot(val slotPosition: ImageSlotPosition): CameraEvent
     *     data object KeepImage: CameraEvent
     *
     *     data class EnableOrDisableCaptureImageButton(val enable: Boolean): CameraEvent
     */

    data class SetName(val name: String): PhotosSummaryEvent
    data class SelectCategory(val category: MaterialCategory): PhotosSummaryEvent
    data object SwitchLightDirections: PhotosSummaryEvent

    data object GoBackToCameraScreen: PhotosSummaryEvent
}

sealed interface PhotosSummaryNavigationEvent {
    data object BackToCameraScreen: PhotosSummaryNavigationEvent
}