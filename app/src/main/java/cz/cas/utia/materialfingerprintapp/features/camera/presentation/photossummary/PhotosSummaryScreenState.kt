package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import android.graphics.Bitmap
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

data class PhotosSummaryScreenState(
    val capturedImageSlot1: Bitmap? = null,
    val capturedImageSlot2: Bitmap? = null,

    val lightDirectionSlot1: LightDirection = LightDirection.FROM_ABOVE,
    val lightDirectionSlot2: LightDirection = LightDirection.FROM_LEFT,

    val materialName: String = "",
    val selectedCategory: MaterialCategory = MaterialCategory.UNCATEGORIZED,

    val isDropdownMenuExpanded: Boolean = false
    )
{
    fun isReadyToAnalyse(): Boolean {
        return capturedImageSlot1 != null && capturedImageSlot2 != null && materialName != "" //todo ted to uzivatele pusti i kdyz napise jen mezeru -> udělat to chytreji a mozna zakazat i nejake specialni znaky
    }
}

enum class LightDirection {
    FROM_LEFT,
    FROM_ABOVE
}