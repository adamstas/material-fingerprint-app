package cz.cas.utia.materialfingerprintapp.features.analytics.data.material.api

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics

data class MaterialCharacteristicsRequestResponse(
    val brightness: Double,
    val checkered_pattern: Double,
    val color_vibrancy: Double,
    val hardness: Double,
    val movement_effect: Double,
    val multicolored: Double,
    val naturalness: Double,
    val pattern_complexity: Double,
    val scale_of_pattern: Double,
    val shininess: Double,
    val sparkle: Double,
    val striped_pattern: Double,
    val surface_roughness: Double,
    val thickness: Double,
    val value: Double,
    val warmth: Double
) {
    fun toMaterialCharacteristics(): MaterialCharacteristics {
        return MaterialCharacteristics(
            brightness = this.brightness,
            checkeredPattern = this.checkered_pattern,
            colorVibrancy = this.color_vibrancy,
            hardness = this.hardness,
            movementEffect = this.movement_effect,
            multicolored = this.multicolored,
            naturalness = this.naturalness,
            patternComplexity = this.pattern_complexity,
            scaleOfPattern = this.scale_of_pattern,
            shininess = this.shininess,
            sparkle = this.sparkle,
            stripedPattern = this.striped_pattern,
            surfaceRoughness = this.surface_roughness,
            thickness = this.thickness,
            value = this.value,
            warmth = this.warmth
        )
    }
}