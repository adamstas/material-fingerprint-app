package cz.cas.utia.materialfingerprintapp.features.analysis.data.material

import android.util.Log
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
       if (photoThumbnail == null)
           Log.i("depuk", "photo thumbnail je null")
       else
           Log.i("depuk", "photo thumbnail neni null")

       return MaterialSummary(
           id = material.id,
           name = material.name,
           photoThumbnail = MaterialImage.BitmapImage(photoThumbnail!!.asImageBitmap()), //todo nechat "!!" ?
           category = material.category,
           characteristics = material.characteristics
       )
   }
}