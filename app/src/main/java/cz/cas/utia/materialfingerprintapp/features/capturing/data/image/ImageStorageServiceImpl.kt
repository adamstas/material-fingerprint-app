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

    override fun storeImage(image: Bitmap, filename: String): String? {
        val imageFile = File(getImagesDirectory(), filename)

        return try {
            FileOutputStream(imageFile).use { outputStream ->
                image.compress(Bitmap.CompressFormat.PNG, 100, outputStream) //quality is ignored since PNG is lossless
            }

            imageFile.absolutePath // todo - vracet neco? zatim je ta navratova hodnota nepouzita
        } catch (e: IOException) { //todo nejak to handlovat? zobrazit v composablech ze nastala chyba?
                                    // asi si udělat nejakou composablu Error(string errorText) a dát ji do core a pak ji volat na obrazovkach, kde nastala chyba (mit ve state promennou na to)

            e.printStackTrace()
            null
        }
    }

    override fun loadImage(filename: String): Bitmap? {
        val imagePath = getImagesDirectory().absolutePath + "/" + filename

        return try {
            BitmapFactory.decodeFile(imagePath)

        } catch (e: Exception) {
            //e.printStackTrace() //todo neprintit stack trace protoze to, ze se vraci prazdny image, je OK v pripade ukladani tech 2 fotek
            null
        }
    }

    override fun loadImageAsFile(filename: String): File {
        return File(getImagesDirectory(), filename) // todo tady to nezarve kdyz image chybi?
    }

    override fun deleteImage(filename: String): Boolean { //todo vracet neco?
        val imagePath = getImagesDirectory().absolutePath + "/" + filename

        val imageFile = File(imagePath)
        return if (imageFile.exists()) {
            imageFile.delete()
        } else {
            false
        }
    }
}