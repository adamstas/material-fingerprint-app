package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Material(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val serverId: Int,
    val name: String,
    val photoPath: String,
    val fingerprintPath: String,
    val category: MaterialCategory,

    val statBrightness: Double,
    val statColorVibrancy: Double,
    val statHardness: Double,
    val statCheckeredPattern: Double,
    val statMovementEffect: Double,
    val statMulticolored: Double,
    val statNaturalness: Double,
    val statPatternComplexity: Double,
    val statScaleOfPattern: Double,
    val statShininess: Double,
    val statSparkle: Double,
    val statStripedPattern: Double,
    val statSurfaceRoughness: Double,
    val statThickness: Double,
    val statValue: Double,
    val statWarmth: Double
)

enum class MaterialCategory {
    FABRIC,
    LEATHER,
    WOOD,
    METAL,
    PLASTIC,
    PAPER,
    COATING,
    UNCATEGORIZED
}
