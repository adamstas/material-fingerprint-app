package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse

import androidx.annotation.StringRes
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary

sealed interface BrowseMaterialsScreenState {
    data class Success(
        val materials: List<MaterialSummary> = emptyList(),
        val checkedMaterials: Set<MaterialSummary> = emptySet(),

        val selectedCategoryIDs: List<Int> = (0..<MaterialCategory.entries.size).toList(), //default all categories selected
        val selectedCategoriesText: String = "All selected",
        val isDropdownMenuExpanded: Boolean = false,

        val searchBarText: String = "",
        val isSearching: Boolean = false,

        val isFindSimilarMaterialButtonEnabled: Boolean = false,
        val isCreatePolarPlotButtonEnabled: Boolean = false,

        val isFindSimilarMaterialsDialogShown: Boolean = false
    ): BrowseMaterialsScreenState
    {
        fun isMaterialChecked(material: MaterialSummary): Boolean {
            return checkedMaterials.contains(material)
        }

        fun isMaterialsListEmpty(): Boolean {
            return materials.isEmpty()
        }

        fun getFirstCheckedMaterial(): MaterialSummary {
            return checkedMaterials.first()
        }
    }

    data class Error(
        @StringRes val messageResId: Int,
        val exception: Throwable
    ): BrowseMaterialsScreenState
}


