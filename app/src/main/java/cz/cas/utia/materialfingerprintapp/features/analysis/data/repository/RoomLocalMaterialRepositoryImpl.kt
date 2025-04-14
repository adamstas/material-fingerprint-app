package cz.cas.utia.materialfingerprintapp.features.analysis.data.repository

import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.MaterialDao
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.CalculateSimilarityUseCase
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.MaterialSummaryMapper
import javax.inject.Inject

class RoomLocalMaterialRepositoryImpl @Inject constructor(
    private val materialDao: MaterialDao,
    private val materialSummaryMapper: MaterialSummaryMapper,
    private val calculateSimilarityUseCase: CalculateSimilarityUseCase
): LocalMaterialRepository {

    override suspend fun insertMaterial(material: Material): Long {
        return materialDao.insertMaterial(material)
    }

    override suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary> {
        val materials = materialDao.getAllMaterialsOrderedByName()
        return materials.map { material -> materialSummaryMapper.map(material) }
    }

    override suspend fun getAllSimilarMaterials(materialId: Long): List<MaterialSummary> {
        val targetMaterial = getMaterial(materialId)
        val targetMaterialSummary = materialSummaryMapper.map(targetMaterial)

        val materials = getAllMaterialsOrderedByName()
        val result = calculateSimilarityUseCase.calculateSimilarity(materials, targetMaterialSummary)

        return result
    }

    override suspend fun getAllSimilarMaterials(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary> {
        val materials = getAllMaterialsOrderedByName()
        return calculateSimilarityUseCase.calculateSimilarity(materials, materialCharacteristics)
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        nameSearch: String
    ): List<MaterialSummary> {
        val effectiveCategories = categories.ifEmpty { MaterialCategory.entries } // if categories are not empty, returns categories

        val materials = materialDao.getMaterialsOrderedByName(effectiveCategories, nameSearch)
        return materials.map { material -> materialSummaryMapper.map(material) }
    }

    override suspend fun getSimilarMaterials(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialId: Long
    ): List<MaterialSummary> {
        val targetMaterial = getMaterial(materialId)
        val targetMaterialSummary = materialSummaryMapper.map(targetMaterial)

        val materials = getAllMaterialsOrderedByName()
        return calculateSimilarityUseCase.calculateSimilarity(materials, targetMaterialSummary, nameSearch, categories)
    }

    override suspend fun getSimilarMaterials(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialCharacteristics: MaterialCharacteristics
    ): List<MaterialSummary> {
        val materials = getAllMaterialsOrderedByName()
        return calculateSimilarityUseCase.calculateSimilarity(materials, materialCharacteristics, nameSearch, categories)
    }

    override suspend fun getMaterial(id: Long): Material {
        return materialDao.getMaterial(id)
    }
}