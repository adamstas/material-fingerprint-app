package cz.cas.utia.materialfingerprintapp.features.analytics.domain

import android.graphics.Bitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter.scaleToDrawingFloats

@Entity
data class Material(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, //ID will be overriden by the generated ID by Room Database
    val serverId: Int?,
    val name: String,
    val photoThumbnailPath: Int,//made int so i can load test images from resources //todo pokud to budu brat proste z ID, tak to odebrat odsud
    val fingerprintPath: Int,//made int so i can load test images from resources //todo pokud to budu brat proste z ID, tak to odebrat odsud
    val category: MaterialCategory,

    @Embedded
    val characteristics: MaterialCharacteristics
)

enum class MaterialCategory {
    FABRIC,
    LEATHER,
    WOOD,
    METAL,
    PLASTIC,
    PAPER,
    COATING,
    UNCATEGORIZED;

    companion object { //so the enum can have "static" method
        //todo osetrit velky index nebo neresit?
        fun fromIDs(ids: List<Int>): List<MaterialCategory> {
            return ids.map { index ->
                MaterialCategory.entries[index]
            }
        }
    }
}

data class MaterialCharacteristics(
    val brightness: Double,
    val colorVibrancy: Double,
    val hardness: Double,
    val checkeredPattern: Double,
    val movementEffect: Double,
    val multicolored: Double,
    val naturalness: Double,
    val patternComplexity: Double,
    val scaleOfPattern: Double,
    val shininess: Double,
    val sparkle: Double,
    val stripedPattern: Double,
    val surfaceRoughness: Double,
    val thickness: Double,
    val value: Double,
    val warmth: Double
) {

    fun toListForDrawing(): List<Float> {
        return listOf(
            scaleToDrawingFloats(brightness),
            scaleToDrawingFloats(colorVibrancy),
            scaleToDrawingFloats(hardness),
            scaleToDrawingFloats(checkeredPattern),
            scaleToDrawingFloats(movementEffect),
            scaleToDrawingFloats(multicolored),
            scaleToDrawingFloats(naturalness),
            scaleToDrawingFloats(patternComplexity),
            scaleToDrawingFloats(scaleOfPattern),
            scaleToDrawingFloats(shininess),
            scaleToDrawingFloats(sparkle),
            scaleToDrawingFloats(stripedPattern),
            scaleToDrawingFloats(surfaceRoughness),
            scaleToDrawingFloats(thickness),
            scaleToDrawingFloats(value),
            scaleToDrawingFloats(warmth)
        )
    }
}

//data needed for displaying the material in the BrowseMaterialsScreen
data class MaterialSummary(
    val id: Long, //todo zatim jen jedno ID takze pokud je to material ze serveru, tak je to id ze sreveru a pokud to ej material z lokalu, tak je lokalni (kdyztak to pak zmenit ze pridam boolean jestli to je ze serveru enbo ne a podle toho se pozna, jake to je id)
    val name: String,
    val photoThumbnail: Bitmap, //no path because these data are not in database
    val fingerprintThumbnail: Bitmap, //todo later will be SVG probably
    val category: MaterialCategory
)