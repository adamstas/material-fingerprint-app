package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.photossummary

import android.graphics.Bitmap
import androidx.annotation.StringRes
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory

sealed interface PhotosSummaryScreenState {
    data class Success(
        val capturedImageSlot1: Bitmap? = null,
        val capturedImageSlot2: Bitmap? = null,

        val lightDirectionSlot1: LightDirection = LightDirection.FROM_LEFT,
        val lightDirectionSlot2: LightDirection = LightDirection.FROM_ABOVE,

        val materialName: String = "",
        val selectedCategory: MaterialCategory = MaterialCategory.UNCATEGORIZED,

        val isDropdownMenuExpanded: Boolean = false,

        val isLoadingDialogShown: Boolean = false,

        val isNameValid: Boolean = false,
        val nameErrorMessage: String = "Name cannot be empty"
    ): PhotosSummaryScreenState
    {
        fun isReadyToAnalyse(): Boolean {
            return capturedImageSlot1 != null &&
                    capturedImageSlot2 != null &&
                    isNameValid
        }
    }

    data class Error(
        @StringRes val messageResId: Int,
        val exception: Throwable
    ): PhotosSummaryScreenState
}

enum class LightDirection {
    FROM_LEFT,
    FROM_ABOVE
}