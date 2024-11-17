package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

interface MaterialRepository {
    suspend fun getAllMaterialsOrderedByName(): List<Material>
    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String): List<Material>
    suspend fun getMaterialsCount(): Int
}