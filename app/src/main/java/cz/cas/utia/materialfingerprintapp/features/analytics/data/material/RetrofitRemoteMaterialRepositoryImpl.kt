package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.RemoteMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

//todo if retrofit not used, change the name
class RetrofitRemoteMaterialRepositoryImpl: RemoteMaterialRepository {
    override suspend fun getAllMaterialsOrderedByName(): List<Material> {
        TODO("Not yet implemented")
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        searchText: String
    ): List<Material> {
        TODO("Not yet implemented")
    }

    override suspend fun getMaterialsCount(): Int {
        TODO("Not yet implemented")
    }
}