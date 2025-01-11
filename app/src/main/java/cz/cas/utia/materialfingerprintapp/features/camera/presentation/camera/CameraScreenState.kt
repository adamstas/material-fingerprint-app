package cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera

import android.graphics.Bitmap

data class CameraScreenState(
    val capturedImage: Bitmap? = null //todo zmenit na neco co nemuze byt null? kdyztak si udelat nejaky EmptyBitmap a ten tam cpat
    )
{

}