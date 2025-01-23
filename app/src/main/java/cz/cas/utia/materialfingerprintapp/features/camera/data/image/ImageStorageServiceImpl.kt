package cz.cas.utia.materialfingerprintapp.features.camera.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileOutputStream
import java.io.File
import java.io.IOException
import javax.inject.Inject

//some inspiration from https://mobterest.medium.com/using-local-file-systems-in-android-native-development-7dbc875e718c
class ImageStorageServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
): ImageStorageService {
    private val imagesDirectoryName = "images"

    private fun getImagesDirectory(): File {
        val imagesDirectory = File(context.filesDir, imagesDirectoryName)

        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdirs()
        }
        return imagesDirectory
    }

    override fun saveImage(image: Bitmap, filename: String): String? {
        val imageFile = File(getImagesDirectory(), filename)

        return try {
            FileOutputStream(imageFile).use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream) //quality is ignored since PNG is lossless
            }
            imageFile.absolutePath // todo - vracet neco?
        } catch (e: IOException) { //todo nejak to handlovat? zobrazit v composablech ze nastala chyba?
            e.printStackTrace()
            null
        }
    }

    override fun loadImage(path: String): Bitmap? {
        return try {
            BitmapFactory.decodeFile(path)
        } catch (e: Exception) {
            e.printStackTrace() //todo neprintit stack trace protoze to, ze se vraci prazdny image, je OK v pripade ukladani tech 2 fotek
            null
        }
    }

    override fun deleteImage(path: String): Boolean { //todo vracet neco?
        val imageFile = File(path)
        return if (imageFile.exists()) {
            imageFile.delete()
        } else {
            false
        }
    }
}