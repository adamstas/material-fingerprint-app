package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.Material

//just to add true/false to each material; not stored in data layer because these checkboxes are just UI thing
data class MaterialUIElement(
    val material: Material,
    val checked: Boolean
)
