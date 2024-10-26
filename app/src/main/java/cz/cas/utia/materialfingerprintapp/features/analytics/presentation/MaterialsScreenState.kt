package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory

data class MaterialsScreenState(
    val materials: List<Material> = emptyList(),
 //todo keep these initial values? or introduce Loaded state etc.?
    val checkedMaterials: Set<Int> = emptySet(),

    val selectedCategoryIDs: List<Int> = (0..<MaterialCategory.entries.size).toList(), //default all categories selected
    val selectedCategoriesText: String = "Selected " + MaterialCategory.entries.size,
    val isDropdownMenuExpanded: Boolean = false,

    val searchBarText: String = "",
    val isSearching: Boolean = false,

    val isFindSimilarMaterialButtonEnabled: Boolean = false,
    val isCreatePolarPlotButtonEnabled: Boolean = false
    )
{
    fun isMaterialChecked(materialID: Int): Boolean {
        return checkedMaterials.contains(materialID)
    }

    fun isMaterialsListEmpty(): Boolean {
        return materials.isEmpty()
    }
}
