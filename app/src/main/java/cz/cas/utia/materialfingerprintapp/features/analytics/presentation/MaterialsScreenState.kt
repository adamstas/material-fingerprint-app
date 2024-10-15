package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory

data class MaterialsScreenState(
    val materials: List<Material> = emptyList(),
    val materialUIElements: List<MaterialUIElement> = emptyList(), //todo keep these initial values? or introduce Loaded state etc.?

    val selectedCategoryIDs: List<Int> = (0..<MaterialCategory.entries.size).toList(), //default all categories selected
    val isDropdownMenuExpanded: Boolean = false

    //todo search bar text
    )
