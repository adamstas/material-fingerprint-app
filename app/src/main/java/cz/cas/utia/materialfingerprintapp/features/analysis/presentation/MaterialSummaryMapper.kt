package cz.cas.utia.materialfingerprintapp.features.analysis.presentation

import androidx.compose.ui.graphics.asImageBitmap
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.IMAGE_SUFFIX
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialImage
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService

class MaterialSummaryMapper(
    private val imageStorageService: ImageStorageService
) {

   fun map(material: Material): MaterialSummary {
       val photoThumbnail = imageStorageService.loadImage(material.id.toString() + IMAGE_SUFFIX)

       return MaterialSummary(
           id = material.id,
           name = material.name,
           photoThumbnail = MaterialImage.BitmapImage(photoThumbnail!!.asImageBitmap()),
           category = material.category,
           characteristics = material.characteristics
       )
   }
}