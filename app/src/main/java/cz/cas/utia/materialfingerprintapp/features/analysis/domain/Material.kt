package cz.cas.utia.materialfingerprintapp.features.analysis.domain

import androidx.compose.ui.graphics.ImageBitmap
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.filter.scaleToDrawingFloats

@Entity(
    indices = [
        Index(value = ["serverId"], unique = true)
    ]
)
data class Material(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0, //ID will be overridden by the generated ID by Room Database
    val serverId: Long?, // not used but stored in case it is needed later
    val name: String,
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
            scaleToDrawingFloats(checkeredPattern),
            scaleToDrawingFloats(surfaceRoughness),
            scaleToDrawingFloats(scaleOfPattern),
            scaleToDrawingFloats(multicolored),
            scaleToDrawingFloats(colorVibrancy),
            scaleToDrawingFloats(brightness),
            scaleToDrawingFloats(naturalness),
            scaleToDrawingFloats(value),
            scaleToDrawingFloats(warmth),
            scaleToDrawingFloats(thickness),
            scaleToDrawingFloats(hardness),
            scaleToDrawingFloats(movementEffect),
            scaleToDrawingFloats(shininess),
            scaleToDrawingFloats(sparkle),
            scaleToDrawingFloats(patternComplexity),
            scaleToDrawingFloats(stripedPattern)
        )
    }
}

//data needed for displaying the material in the BrowseMaterialsScreen
data class MaterialSummary(
    val id: Long, // if material is from server then this is remote ID, otherwise it is local ID
    val name: String,
    val photoThumbnail: MaterialImage,
    val category: MaterialCategory,
    val characteristics: MaterialCharacteristics
)

sealed class MaterialImage {
    data class BitmapImage(val imageBitmap: ImageBitmap): MaterialImage()
    data object UrlImage: MaterialImage()
}