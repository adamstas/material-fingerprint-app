package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import androidx.annotation.StringRes
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary

sealed interface MaterialsScreenState {
    //todo rename to BrowseMaterialsScreenState? if new material screens are added...
    data class Success(
        val materials: List<MaterialSummary> = emptyList(),
        //todo keep these initial values? or introduce Loaded state etc.?
        val checkedMaterials: Set<MaterialSummary> = emptySet(),

        val selectedCategoryIDs: List<Int> = (0..<MaterialCategory.entries.size).toList(), //default all categories selected
        val selectedCategoriesText: String = "All selected",
        val isDropdownMenuExpanded: Boolean = false,

        val searchBarText: String = "",
        val isSearching: Boolean = false,

        val isFindSimilarMaterialButtonEnabled: Boolean = false,
        val isCreatePolarPlotButtonEnabled: Boolean = false,

        val isFindSimilarMaterialsDialogShown: Boolean = false
    ): MaterialsScreenState
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
    ): MaterialsScreenState
}


