package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import android.graphics.Bitmap
import android.util.Log
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary
import kotlinx.coroutines.delay
import javax.inject.Inject

//todo rename to RoomLocalMaterialRepositoryImpl ?
class RoomMaterialRepositoryImpl @Inject constructor(
    private val materialDao: MaterialDao,
    private val materialSummaryMapper: MaterialSummaryMapper
): LocalMaterialRepository {

    override suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary> {
        if (getMaterialsCount() == 0) //todo remove later, now just for seeding the database
            materialDao.insertMaterials(initialData())

        val materials = materialDao.getAllMaterialsOrderedByName()
        return materials.map { material -> materialSummaryMapper.map(material) }
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        searchText: String
    ): List<MaterialSummary> {
        //delay(1000) //todo remove later

        val materials = materialDao.getMaterialsOrderedByName(categories, searchText)
        Log.i("depuk", "materials v room impl jsou: $materials")
        return materials.map { material -> materialSummaryMapper.map(material) }
    }

    override suspend fun getMaterialsCount(): Int {
        return materialDao.getMaterialsCount()
    }

    override suspend fun getMaterial(id: Int): Material {
        return materialDao.getMaterial(id)
    }

    override suspend fun getPolarPlot(id: Int): Bitmap {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMaterialsSortedBySimilarity(characteristics: MaterialCharacteristics): List<MaterialSummary> {
        TODO("Not yet implemented")
    }
}