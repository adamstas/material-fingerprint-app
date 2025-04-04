package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.CalculateSimilarityUseCase
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary
import javax.inject.Inject

//todo rename to RoomLocalMaterialRepositoryImpl ?
class RoomMaterialRepositoryImpl @Inject constructor(
    private val materialDao: MaterialDao,
    private val materialSummaryMapper: MaterialSummaryMapper,
    private val calculateSimilarityUseCase: CalculateSimilarityUseCase
): LocalMaterialRepository {

    override suspend fun insertMaterial(material: Material): Long {
        return materialDao.insertMaterial(material)
    }

    override suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary> {
        if (getMaterialsCount() == 0L) //todo remove later, now just for seeding the database
            materialDao.insertMaterials(initialData())

        val materials = materialDao.getAllMaterialsOrderedByName()
        return materials.map { material -> materialSummaryMapper.map(material) }
    }

    override suspend fun getAllSimilarMaterialsOrderedByName(materialId: Long): List<MaterialSummary> {
        val targetMaterial = materialDao.getMaterial(materialId) // todo lze pak volat rovnou metodu getMaterial toho repozitáře a ne toho dao (pokud to tu zustane)
        val targetMaterialSummary = materialSummaryMapper.map(targetMaterial)

        val materials = getAllMaterialsOrderedByName()
        val result = calculateSimilarityUseCase.calculateSimilarity(materials, targetMaterialSummary)

        return result
    }

    override suspend fun getAllSimilarMaterialsOrderedByName(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary> {
        val materials = getAllMaterialsOrderedByName()
        return calculateSimilarityUseCase.calculateSimilarity(materials, materialCharacteristics)
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        nameSearch: String
    ): List<MaterialSummary> {
        //delay(1000) //todo remove later

        val materials = materialDao.getMaterialsOrderedByName(categories, nameSearch)
        return materials.map { material -> materialSummaryMapper.map(material) }
    }

    override suspend fun getSimilarMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialId: Long
    ): List<MaterialSummary> {
        val targetMaterial = materialDao.getMaterial(materialId)  // todo lze pak volat rovnou metodu getMaterial toho repozitáře a ne toho dao (pokud to tu zustane)
        val targetMaterialSummary = materialSummaryMapper.map(targetMaterial)

        val materials = getAllMaterialsOrderedByName()
        return calculateSimilarityUseCase.calculateSimilarity(materials, targetMaterialSummary, nameSearch, categories)
    }

    override suspend fun getSimilarMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialCharacteristics: MaterialCharacteristics
    ): List<MaterialSummary> {
        val materials = getAllMaterialsOrderedByName()
        return calculateSimilarityUseCase.calculateSimilarity(materials, materialCharacteristics, nameSearch, categories)
    }

    override suspend fun getMaterialsCount(): Long {
        return materialDao.getMaterialsCount()
    }

    override suspend fun getMaterial(id: Long): Material {
        return materialDao.getMaterial(id)
    }
}