package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory

data class MaterialsScreenState(
    val materials: List<MaterialUIElement> = emptyList(), //todo keep these initial values? or introduce Loaded state etc.?
    val selectedCategories: List<MaterialCategory> = MaterialCategory.entries //default all categories selected

    //todo search bar text
    )
