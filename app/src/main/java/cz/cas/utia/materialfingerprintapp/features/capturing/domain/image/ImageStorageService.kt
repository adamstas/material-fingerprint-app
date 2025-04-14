package cz.cas.utia.materialfingerprintapp.features.capturing.domain.image

import android.graphics.Bitmap
import java.io.File

interface ImageStorageService {
    fun storeImage(image: Bitmap, filename: String)
    fun loadImage(filename: String): Bitmap?
    fun loadImageAsFile(filename: String): File
    fun deleteImage(filename: String): Boolean
}