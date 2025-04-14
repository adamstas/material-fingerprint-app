package cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material

interface LocalMaterialRepository: MaterialRepository {
    suspend fun insertMaterial(material: Material): Long
    suspend fun getMaterial(id: Long): Material
}