package cz.cas.utia.materialfingerprintapp.features.analysis.presentation

import androidx.compose.ui.graphics.asImageBitmap
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.IMAGE_SUFFIX
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialImage
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.MaterialImageType

class MaterialSummaryMapper(
    private val imageStorageService: ImageStorageService
) {

    private fun loadMaterialImage(
        materialId: Long,
        type: MaterialImageType
    ): MaterialImage {
        val bitmap = imageStorageService.loadImage(
            filename = materialId.toString() + IMAGE_SUFFIX,
            type = type
        )

        return MaterialImage.BitmapImage(bitmap!!.asImageBitmap())
    }

   fun map(material: Material): MaterialSummary {
       return MaterialSummary(
           id = material.id,
           name = material.name,
           specularPhotoThumbnail = loadMaterialImage(materialId = material.id, type = MaterialImageType.SPECULAR),
           nonSpecularPhotoThumbnail = loadMaterialImage(materialId = material.id, type = MaterialImageType.NON_SPECULAR),
           category = material.category,
           characteristics = material.characteristics
       )
   }
}