package cz.cas.utia.materialfingerprintapp.features.analysis.data.repository

import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.IMAGE_SUFFIX
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SLOT1_IMAGE_NAME
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SLOT2_IMAGE_NAME
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.MaterialApiService
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.MaterialCharacteristicsRequestResponse
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.SimilarMaterialsRequest
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.RemoteMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
import cz.cas.utia.materialfingerprintapp.features.capturing.presentation.photossummary.LightDirection
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RetrofitRemoteMaterialRepositoryImpl @Inject constructor(
    private val materialApiService: MaterialApiService,
    private val imageStorageService: ImageStorageService
): RemoteMaterialRepository {

    private fun fromMaterialCharacteristicsToRequestResponse(characteristics: MaterialCharacteristics)
    : MaterialCharacteristicsRequestResponse {
        return MaterialCharacteristicsRequestResponse(
            brightness = characteristics.brightness,
            checkered_pattern = characteristics.checkeredPattern,
            color_vibrancy = characteristics.colorVibrancy,
            hardness = characteristics.hardness,
            movement_effect = characteristics.movementEffect,
            multicolored = characteristics.multicolored,
            naturalness = characteristics.naturalness,
            pattern_complexity = characteristics.patternComplexity,
            scale_of_pattern = characteristics.scaleOfPattern,
            shininess = characteristics.shininess,
            sparkle = characteristics.sparkle,
            striped_pattern = characteristics.stripedPattern,
            surface_roughness = characteristics.surfaceRoughness,
            thickness = characteristics.thickness,
            value = characteristics.value,
            warmth = characteristics.warmth
        )
    }

    private fun createImagePart(filename: String, partName: String): MultipartBody.Part {
        val file = imageStorageService.loadImageAsFile(filename + IMAGE_SUFFIX)
        val requestBody = file.asRequestBody("image/png".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(name = partName, filename = filename, body = requestBody)
    }

    override suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary> {
        val responseMaterials = materialApiService.getMaterialsOrderedByName()
        return responseMaterials.map { it.toMaterialSummary() }
    }

    override suspend fun getAllSimilarMaterials(materialId: Long): List<MaterialSummary> {
        val responseMaterials = materialApiService.getSimilarMaterialsOrderedByName(materialId)
        return responseMaterials.map { it.toMaterialSummary() }
    }

    override suspend fun getAllSimilarMaterials(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary> {
        val body = SimilarMaterialsRequest(
            characteristics = fromMaterialCharacteristicsToRequestResponse(materialCharacteristics)
        )
        val responseMaterials = materialApiService.getSimilarMaterialsByCharacteristicsOrderedByName(body)

        return responseMaterials.map { it.toMaterialSummary() }
    }

    override suspend fun getMaterialsOrderedByName(
        categories: List<MaterialCategory>,
        nameSearch: String
    ): List<MaterialSummary> {
        val responseMaterials = materialApiService.getMaterialsOrderedByName(
            name = nameSearch,
            categories = categories.map { it.toString() }
        )
        return responseMaterials.map { it.toMaterialSummary() }
    }

    override suspend fun getSimilarMaterials(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialId: Long
    ): List<MaterialSummary> {
        val responseMaterials = materialApiService.getSimilarMaterialsOrderedByName(
            materialId = materialId,
            name = nameSearch,
            categories = categories.map { it.toString() })
        return responseMaterials.map { it.toMaterialSummary() }
    }

    override suspend fun getSimilarMaterials(
        categories: List<MaterialCategory>,
        nameSearch: String,
        materialCharacteristics: MaterialCharacteristics
    ): List<MaterialSummary> {
        val body = SimilarMaterialsRequest(
            characteristics = fromMaterialCharacteristicsToRequestResponse(materialCharacteristics),
            name = nameSearch,
            categories = categories.map { it.toString() }
        )
        val responseMaterials = materialApiService.getSimilarMaterialsByCharacteristicsOrderedByName(body)

        return responseMaterials.map { it.toMaterialSummary() }
    }

    override suspend fun analyseMaterial(
        firstImageLightDirection: LightDirection,
        name: String,
        category: MaterialCategory,
        storeInDb: Boolean
    ): Material {
        val (specularFilename, nonSpecularFilename) = when (firstImageLightDirection) {
            LightDirection.FROM_ABOVE -> SLOT1_IMAGE_NAME to SLOT2_IMAGE_NAME
            LightDirection.FROM_LEFT -> SLOT2_IMAGE_NAME to SLOT1_IMAGE_NAME
        }

        val specularImage = createImagePart(filename = specularFilename, partName = "specular_image")
        val nonSpecularImage = createImagePart(filename = nonSpecularFilename, partName = "non_specular_image")

        val nameBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val categoryBody = category.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val storeInDbBody = storeInDb.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val responseMaterial = materialApiService.analyseMaterial(
            name = nameBody,
            category = categoryBody,
            storeInDb = storeInDbBody,
            specular_image = specularImage,
            non_specular_image = nonSpecularImage
        )

        return responseMaterial.toMaterial()
    }
}