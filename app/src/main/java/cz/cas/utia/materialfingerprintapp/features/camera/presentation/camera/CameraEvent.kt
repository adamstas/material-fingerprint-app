package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

import android.graphics.Bitmap

//todo keep naming convention so this is CameraEvent and in material screen there is MaterialEvent?
sealed interface CameraEvent {
    data class CaptureImage(val imageBitmap: Bitmap): CameraEvent //todo or rename to OnImageCaptured?
    data object RetakeImage: CameraEvent //todo later make class instead of object and add slot (1 or 2)
    //todo rename retake to clearImage?
}