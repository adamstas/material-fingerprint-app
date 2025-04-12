package cz.cas.utia.materialfingerprintapp.features.analytics.data.material.api

data class SimilarMaterialsRequest(
    val characteristics: MaterialCharacteristicsRequestResponse,
    val name: String? = null,
    val categories: List<String>? = null
)