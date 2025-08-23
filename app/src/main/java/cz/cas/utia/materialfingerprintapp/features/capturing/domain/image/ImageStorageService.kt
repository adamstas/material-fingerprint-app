package cz.cas.utia.materialfingerprintapp.features.capturing.domain.image

import android.graphics.Bitmap
import java.io.File

interface ImageStorageService {
    fun storeImage(image: Bitmap, filename: String, type: MaterialImageType)
    fun loadImage(filename: String, type: MaterialImageType): Bitmap?
    fun loadImageAsFile(filename: String, type: MaterialImageType): File
    fun deleteImage(filename: String, type: MaterialImageType): Boolean
}

enum class MaterialImageType(val folderName: String) {
    SPECULAR("specular"),
    NON_SPECULAR("nonspecular"),
    SLOT("slot")
}