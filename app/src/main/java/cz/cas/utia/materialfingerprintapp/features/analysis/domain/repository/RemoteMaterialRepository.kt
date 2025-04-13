package cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.capturing.presentation.photossummary.LightDirection

interface RemoteMaterialRepository: MaterialRepository {
    suspend fun analyseMaterial(
        firstImageLightDirection: LightDirection,
        name: String,
        category: MaterialCategory,
        storeInDb: Boolean
    ): Material
}