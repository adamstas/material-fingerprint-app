package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary

//todo rename to BrowseMaterialsScreenState? if new material screens are added...
data class MaterialsScreenState(
    val materials: List<MaterialSummary> = emptyList(),
 //todo keep these initial values? or introduce Loaded state etc.?
    val checkedMaterials: Set<Long> = emptySet(),

    val selectedCategoryIDs: List<Int> = (0..<MaterialCategory.entries.size).toList(), //default all categories selected
    val selectedCategoriesText: String = "All selected",
    val isDropdownMenuExpanded: Boolean = false,

    val searchBarText: String = "",
    val isSearching: Boolean = false,

    val isFindSimilarMaterialButtonEnabled: Boolean = false,
    val isCreatePolarPlotButtonEnabled: Boolean = false
    )
{
    fun isMaterialChecked(materialID: Long): Boolean {
        return checkedMaterials.contains(materialID)
    }

    fun isMaterialsListEmpty(): Boolean {
        return materials.isEmpty()
    }

    fun getFirstCheckedMaterialId(): Long {
        return checkedMaterials.first()
    }
}
