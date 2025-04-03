package cz.cas.utia.materialfingerprintapp.features.camera.domain.image

import android.graphics.Bitmap
import java.io.File

//todo byt konzistentni takze bud toto dat pryc z domain do data anebo v analytics -> data -> repository to dat do domain
interface ImageStorageService {
    fun storeImage(image: Bitmap, filename: String): String?
    fun loadImage(filename: String): Bitmap?
    fun loadImageAsFile(filename: String): File
    fun deleteImage(filename: String): Boolean
}