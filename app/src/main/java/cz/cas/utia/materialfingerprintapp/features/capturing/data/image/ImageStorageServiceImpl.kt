package cz.cas.utia.materialfingerprintapp.features.capturing.data.image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
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

    override fun storeImage(image: Bitmap, filename: String) {
        val imageFile = File(getImagesDirectory(), filename)

        try {
            FileOutputStream(imageFile).use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream) //quality is ignored since PNG is lossless
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun loadImage(filename: String): Bitmap? {
        val imagePath = getImagesDirectory().absolutePath + "/" + filename

        return try {
            BitmapFactory.decodeFile(imagePath)

        } catch (e: Exception) {
            null // do not throw exception because in CapturingViewModel it may be OK that images are not found
        }
    }

    override fun loadImageAsFile(filename: String): File {
        return File(getImagesDirectory(), filename)
    }

    override fun deleteImage(filename: String): Boolean {
        val imagePath = getImagesDirectory().absolutePath + "/" + filename

        val imageFile = File(imagePath)
        return if (imageFile.exists()) {
            imageFile.delete()
        } else {
            false
        }
    }
}