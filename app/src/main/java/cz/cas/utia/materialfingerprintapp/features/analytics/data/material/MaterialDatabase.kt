package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics

@Database(
    entities = [Material::class],
    version = 1 //todo check how to migrate and maybe implement it here
)
abstract class MaterialDatabase: RoomDatabase() {

    abstract val materialDao: MaterialDao
}

//todo move somewhere else?
fun initialData(): List<Material> {
    return listOf(
        Material(
            //id = -1, //some dummy ID, will be overriden
            serverId = 1,
            name = "Leather1",
            photoThumbnailPath = R.drawable.latka,
            fingerprintPath = R.drawable.latkapolarplot,
            category = MaterialCategory.LEATHER,
            characteristics = MaterialCharacteristics(
                brightness = 0.85,
                colorVibrancy = 0.75,
                hardness = 0.10,
                checkeredPattern = 0.0,
                movementEffect = 0.3,
                multicolored = 0.1,
                naturalness = 0.95,
                patternComplexity = 0.1,
                scaleOfPattern = 0.2,
                shininess = 0.9,
                sparkle = 0.5,
                stripedPattern = 0.0,
                surfaceRoughness = 0.1,
                thickness = 0.15,
                value = 90.0,
                warmth = 0.7
            )
        ),

        Material(
            //id = -1, //some dummy ID, will be overriden
            serverId = 2,
            name = "Leather2",
            photoThumbnailPath = R.drawable.latka2,
            fingerprintPath = R.drawable.latka2polarplot,
            category = MaterialCategory.LEATHER,
            characteristics = MaterialCharacteristics(
                brightness = 0.45,
                colorVibrancy = 0.15,
                hardness = 0.19,
                checkeredPattern = 0.9,
                movementEffect = 0.0,
                multicolored = 0.4,
                naturalness = 0.95,
                patternComplexity = 0.7,
                scaleOfPattern = 0.8,
                shininess = 0.4,
                sparkle = 0.1,
                stripedPattern = 0.4,
                surfaceRoughness = 0.0,
                thickness = 0.18,
                value = 94.0,
                warmth = 0.78
            )
        ),

        Material(
           // id = -1, //some dummy ID, will be overriden
            serverId = 3,
            name = "Leather3",
            photoThumbnailPath = R.drawable.latka3,
            fingerprintPath = R.drawable.latka3polarplot,
            category = MaterialCategory.LEATHER,
            characteristics = MaterialCharacteristics(
                brightness = 0.45,
                colorVibrancy = 0.15,
                hardness = 0.19,
                checkeredPattern = 0.9,
                movementEffect = 0.7,
                multicolored = 0.4,
                naturalness = 0.95,
                patternComplexity = 0.0,
                scaleOfPattern = 0.8,
                shininess = 0.4,
                sparkle = 0.1,
                stripedPattern = 0.4,
                surfaceRoughness = 0.0,
                thickness = 0.18,
                value = 94.0,
                warmth = 0.78
            )
        ),

        Material(
           // id = -1, //some dummy ID, will be overriden
            serverId = 4,
            name = "MetalSObrazkemLatky",
            photoThumbnailPath = R.drawable.latka4,
            fingerprintPath = R.drawable.latka4polarplot,
            category = MaterialCategory.METAL,
            characteristics = MaterialCharacteristics(
                brightness = 0.45,
                colorVibrancy = 0.15,
                hardness = 0.19,
                checkeredPattern = 0.9,
                movementEffect = 0.0,
                multicolored = 0.5,
                naturalness = 0.95,
                patternComplexity = 0.7,
                scaleOfPattern = 0.8,
                shininess = 0.4,
                sparkle = 0.3,
                stripedPattern = 0.4,
                surfaceRoughness = 0.0,
                thickness = 0.18,
                value = 94.0,
                warmth = 0.78
            )
        ),

        Material(
            // id = -1, //some dummy ID, will be overriden
            serverId = 5,
            name = "WoodSObrazkemLatky",
            photoThumbnailPath = R.drawable.latka2,
            fingerprintPath = R.drawable.latka2polarplot,
            category = MaterialCategory.WOOD,
            characteristics = MaterialCharacteristics(
                brightness = 0.45,
                colorVibrancy = 0.15,
                hardness = 0.19,
                checkeredPattern = 0.9,
                movementEffect = 0.0,
                multicolored = 0.7,
                naturalness = 0.95,
                patternComplexity = 0.7,
                scaleOfPattern = 0.4,
                shininess = 0.4,
                sparkle = 0.1,
                stripedPattern = 0.4,
                surfaceRoughness = 0.0,
                thickness = 0.18,
                value = 94.0,
                warmth = 0.78
            )
        )
    )
}