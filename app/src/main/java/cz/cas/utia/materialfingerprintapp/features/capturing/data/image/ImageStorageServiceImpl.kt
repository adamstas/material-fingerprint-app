package cz.cas.utia.materialfingerprintapp.features.capturing.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.MaterialImageType
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

    private fun getImagesDirectory(type: MaterialImageType): File {
        val imagesDirectory = File(context.filesDir, "$imagesDirectoryName/${type.folderName}")

        if (!imagesDirectory.exists()) {
            imagesDirectory.mkdirs()
        }
        return imagesDirectory
    }

    override fun storeImage(image: Bitmap, filename: String, type: MaterialImageType) {
        val imageFile = File(getImagesDirectory(type), filename)

        try {
            FileOutputStream(imageFile).use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream) //quality is ignored since PNG is lossless
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun loadImage(filename: String, type: MaterialImageType): Bitmap? {
        val imagePath = getImagesDirectory(type).absolutePath + "/" + filename

        return try {
            BitmapFactory.decodeFile(imagePath)

        } catch (e: Exception) {
            null // do not throw exception because in CapturingViewModel it may be OK that images are not found
        }
    }

    override fun loadImageAsFile(filename: String, type: MaterialImageType): File {
        return File(getImagesDirectory(type), filename)
    }

    override fun deleteImage(filename: String, type: MaterialImageType): Boolean {
        val imagePath = getImagesDirectory(type).absolutePath + "/" + filename

        val imageFile = File(imagePath)
        return if (imageFile.exists()) {
            imageFile.delete()
        } else {
            false
        }
    }
}