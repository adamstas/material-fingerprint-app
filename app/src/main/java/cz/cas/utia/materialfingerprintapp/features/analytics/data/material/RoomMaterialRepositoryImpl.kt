package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary
import javax.inject.Inject

//todo rename to RoomLocalMaterialRepositoryImpl ?
class RoomMaterialRepositoryImpl @Inject constructor(
    private val materialDao: MaterialDao,
    private val materialSummaryMapper: MaterialSummaryMapper
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
        //TODO fixnout
        val materials = materialDao.getAllMaterialsOrderedByName()
        val mapped = materials.map { material -> materialSummaryMapper.map(material) }
        return listOf(mapped[0], mapped[1])
    }

    override suspend fun getAllSimilarMaterialsOrderedByName(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary> {
        //TODO fixnout
        val materials = materialDao.getAllMaterialsOrderedByName()
        val mapped = materials.map { material -> materialSummaryMapper.map(material) }
        return listOf(mapped[0], mapped[1])
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
        //TODO fixnout
        val materials = materialDao.getMaterialsOrderedByName(categories, nameSearch)
        val mapped = materials.map { material -> materialSummaryMapper.map(material) }

        if (mapped.isEmpty())
            return mapped

        val resList = emptyList<MaterialSummary>().toMutableList()
        resList += mapped[0]
        return resList
    }

    override suspend fun getSimilarMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialCharacteristics: MaterialCharacteristics
    ): List<MaterialSummary> {
        //TODO fixnout
        val materials = materialDao.getMaterialsOrderedByName(categories, nameSearch)
        val mapped = materials.map { material -> materialSummaryMapper.map(material) }

        if (mapped.isEmpty())
            return mapped

        val resList = emptyList<MaterialSummary>().toMutableList()
        resList += mapped[0]
        return resList
    }

    override suspend fun getMaterialsCount(): Long {
        return materialDao.getMaterialsCount()
    }

    override suspend fun getMaterial(id: Long): Material {
        return materialDao.getMaterial(id)
    }
}