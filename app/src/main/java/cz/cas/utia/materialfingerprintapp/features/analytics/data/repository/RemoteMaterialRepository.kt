package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary.LightDirection

interface RemoteMaterialRepository: MaterialRepository {
    suspend fun analyseMaterial(firstImageLightDirection: LightDirection, name: String, category: MaterialCategory): Material
}