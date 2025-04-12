package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing

import android.graphics.Bitmap

data class CapturingScreenState(
    val capturedImageSlot1: Bitmap? = null, //todo zmenit na neco co nemuze byt null? kdyztak si udelat nejaky EmptyBitmap a ten tam cpat
    val capturedImageSlot2: Bitmap? = null,
    val currentlyCapturedImage: Bitmap? = null, //just captured, not saved in any slot
    val selectedImageSlot: ImageSlotPosition = ImageSlotPosition.FIRST,

    val isDialogShown: Boolean = false,
    val isCaptureImageButtonEnabled: Boolean = false
    )
{
    fun isSlotSelected(slot: ImageSlotPosition): Boolean {
        return slot == selectedImageSlot
    }
}

enum class ImageSlotPosition {
    FIRST,
    SECOND
}