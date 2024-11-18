package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import kotlinx.coroutines.delay
import javax.inject.Inject

class RoomMaterialRepositoryImpl @Inject constructor(
    private val materialDao: MaterialDao
): LocalMaterialRepository {

    override suspend fun getAllMaterialsOrderedByName(): List<Material> {
        return materialDao.getAllMaterialsOrderedByName()
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        searchText: String
    ): List<Material> {
        //delay(1000) //todo remove later
        return materialDao.getMaterialsOrderedByName(categories, searchText)
    }

    override suspend fun getMaterialsCount(): Int {
        return materialDao.getMaterialsCount()
    }
}