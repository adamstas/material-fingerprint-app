package cz.cas.utia.materialfingerprintapp.features.camera.domain.image

import android.graphics.Bitmap

//todo byt konzistentni takze bud toto dat pryc z domain do data anebo v analytics -> data -> repository to dat do domain
interface ImageStorageService {
    fun saveImage(image: Bitmap, filename: String): String?
    fun loadImage(path: String): Bitmap?
    fun deleteImage(path: String): Boolean
}//todo pozdeji tuhle service pouzit i pri ukladani obrazku do DB (ulozit je do filesystemu pomoci servicky a pak i do DB tu cestu)