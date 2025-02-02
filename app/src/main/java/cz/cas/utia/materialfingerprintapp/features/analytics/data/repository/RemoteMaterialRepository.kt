package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import android.graphics.Bitmap
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

interface RemoteMaterialRepository: MaterialRepository {
    //todo ty dva obrazky lze nacitat z interniho uloziste, ale predavani je asi lepsi (jinak by si musel nejak stejne pamatovat, ktery je specular a ktery nonspecular)
    //todo nechat to jako bitmapy? udelat z nich nejake PNG
    suspend fun analyseMaterial(specularImage: Bitmap, nonSpecularImage: Bitmap, name: String, category: MaterialCategory): Material //todo returns Material that was stored in server DB and that needs to be stored in local DB - maybe return some tuple and create Material from it (because of local ID)

    suspend fun getPolarPlots(firstMaterial: Material, secondMaterial: Material): Bitmap //todo return two SVGs and their combination = total 3 SVGs
}