package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import android.util.Log
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService

class MaterialSummaryMapper(
    private val imageStorageService: ImageStorageService
) {

   fun map(material: Material): MaterialSummary {
       val photoThumbnail = imageStorageService.loadImage(material.id.toString()) //todo ukladat images podle id
       val fingerprintThumbnail = imageStorageService.loadImage("fingerprint" + material.id.toString()) //todo pak predelat z bitmap na svg a asi na to mit specialni metodu v ImageSTorageService a nacitat z jineho adresare
       if (photoThumbnail == null)
           Log.i("depuk", "photo thumbnail je null")
       else
           Log.i("depuk", "photo thumbnail neni null")

       if (fingerprintThumbnail == null)
           Log.i("depuk", "fingerprint thumbnail je null")
       else
           Log.i("depuk", "fingerprint thumbnail neni null")

       return MaterialSummary(
           id = material.id,
           name = material.name,
           photoThumbnail = photoThumbnail!!, //todo nechat "!!" ?
           fingerprintThumbnail = fingerprintThumbnail!!,
           category = material.category
       )
   }
}