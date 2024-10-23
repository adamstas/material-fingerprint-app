package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.cas.utia.materialfingerprintapp.R

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
            photoPath = R.drawable.latka,
            fingerprintPath = R.drawable.latkapolarplot,
            category = MaterialCategory.LEATHER,
            statBrightness = 0.85,
            statColorVibrancy = 0.75,
            statHardness = 0.10,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.3,
            statMulticolored = 0.1,
            statNaturalness = 0.95,
            statPatternComplexity = 0.1,
            statScaleOfPattern = 0.2,
            statShininess = 0.9,
            statSparkle = 0.5,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.1,
            statThickness = 0.15,
            statValue = 90.0,
            statWarmth = 0.7
        ),

        Material(
            //id = -1, //some dummy ID, will be overriden
            serverId = 2,
            name = "Leather2",
            photoPath = R.drawable.latka2,
            fingerprintPath = R.drawable.latka2polarplot,
            category = MaterialCategory.LEATHER,
            statBrightness = 0.5,
            statColorVibrancy = 0.4,
            statHardness = 0.9,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.0,
            statMulticolored = 0.1,
            statNaturalness = 0.95,
            statPatternComplexity = 0.2,
            statScaleOfPattern = 0.6,
            statShininess = 0.3,
            statSparkle = 0.0,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.8,
            statThickness = 0.9,
            statValue = 60.0,
            statWarmth = 0.8
        ),

        Material(
           // id = -1, //some dummy ID, will be overriden
            serverId = 3,
            name = "Leather3",
            photoPath = R.drawable.latka3,
            fingerprintPath = R.drawable.latka3polarplot,
            category = MaterialCategory.LEATHER,
            statBrightness = 0.5,
            statColorVibrancy = 0.4,
            statHardness = 0.9,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.08,
            statMulticolored = 0.51,
            statNaturalness = 0.95,
            statPatternComplexity = 0.2,
            statScaleOfPattern = 0.6,
            statShininess = 0.3,
            statSparkle = 0.0,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.41,
            statThickness = 0.9,
            statValue = 60.0,
            statWarmth = 0.8
        ),

        Material(
           // id = -1, //some dummy ID, will be overriden
            serverId = 4,
            name = "MetalSObrazkemLatky",
            photoPath = R.drawable.latka4,
            fingerprintPath = R.drawable.latka4polarplot,
            category = MaterialCategory.METAL,
            statBrightness = 0.99,
            statColorVibrancy = 0.4,
            statHardness = 0.9,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.08,
            statMulticolored = 0.51,
            statNaturalness = 0.95,
            statPatternComplexity = 0.2,
            statScaleOfPattern = 0.6,
            statShininess = 0.3,
            statSparkle = 0.0,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.41,
            statThickness = 0.9,
            statValue = 60.0,
            statWarmth = 0.8
        ),

        Material(
            // id = -1, //some dummy ID, will be overriden
            serverId = 5,
            name = "WoodSObrazkemLatky",
            photoPath = R.drawable.latka2,
            fingerprintPath = R.drawable.latka2polarplot,
            category = MaterialCategory.WOOD,
            statBrightness = 0.99,
            statColorVibrancy = 0.4,
            statHardness = 0.91,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.08,
            statMulticolored = 0.45,
            statNaturalness = 0.95,
            statPatternComplexity = 0.2,
            statScaleOfPattern = 0.56,
            statShininess = 0.3,
            statSparkle = 0.0,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.41,
            statThickness = 0.9,
            statValue = 60.0,
            statWarmth = 0.8
        )
    )
}