package cz.cas.utia.materialfingerprintapp.features.capturing.domain.image

import android.graphics.Bitmap
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.NON_SPECULAR_IMAGES_PATH
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SLOT_IMAGES_PATH
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SPECULAR_IMAGES_PATH
import java.io.File

interface ImageStorageService {
    fun storeImage(image: Bitmap, filename: String, type: MaterialImageType)
    fun loadImage(filename: String, type: MaterialImageType): Bitmap?
    fun loadImageAsFile(filename: String, type: MaterialImageType): File
    fun deleteImage(filename: String, type: MaterialImageType): Boolean
}

enum class MaterialImageType {
    SPECULAR,
    NON_SPECULAR,
    SLOT
}

fun materialImageTypeToFolderName(type: MaterialImageType): String {
    return when (type) {
        MaterialImageType.SPECULAR -> SPECULAR_IMAGES_PATH
        MaterialImageType.NON_SPECULAR -> NON_SPECULAR_IMAGES_PATH
        MaterialImageType.SLOT -> SLOT_IMAGES_PATH
    }
}