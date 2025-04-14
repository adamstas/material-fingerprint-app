package cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary

interface MaterialRepository {
    suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary>
    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, nameSearch: String): List<MaterialSummary>

    suspend fun getAllSimilarMaterials(materialId: Long): List<MaterialSummary>
    suspend fun getAllSimilarMaterials(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    suspend fun getSimilarMaterials(categories: List<MaterialCategory>, nameSearch: String, materialId: Long): List<MaterialSummary>
    suspend fun getSimilarMaterials(categories: List<MaterialCategory>, nameSearch: String, materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>
}