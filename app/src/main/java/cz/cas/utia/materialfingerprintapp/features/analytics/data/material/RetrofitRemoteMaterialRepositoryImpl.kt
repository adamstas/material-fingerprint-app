package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import android.graphics.Bitmap
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.RemoteMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary

//todo if retrofit not used, change the name
class RetrofitRemoteMaterialRepositoryImpl: RemoteMaterialRepository {

    override suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSimilarMaterialsOrderedByName(materialId: Long): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllSimilarMaterialsOrderedByName(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        searchText: String
    ): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        searchText: String,
        materialId: Long
    ): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getSimilarMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        searchText: String,
        materialCharacteristics: MaterialCharacteristics
    ): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getMaterialsCount(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun analyseMaterial(
        specularImage: Bitmap,
        nonSpecularImage: Bitmap,
        name: String,
        category: MaterialCategory
    ): Material {
        TODO("Not yet implemented")
    }

    override suspend fun getAllMaterialsSortedBySimilarity(characteristics: MaterialCharacteristics): List<MaterialSummary> {
        TODO("Not yet implemented")
    }

    override suspend fun getPolarPlots(firstMaterial: Material, secondMaterial: Material): Bitmap {
        TODO("Not yet implemented")
    }

    override suspend fun getMaterial(id: Long): Material {
        TODO("Not yet implemented")
    }
}