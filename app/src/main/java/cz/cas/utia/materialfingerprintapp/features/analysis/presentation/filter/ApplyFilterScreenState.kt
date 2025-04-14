package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.filter

import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCharacteristics

data class ApplyFilterScreenState(
    val materialCharacteristics: MaterialCharacteristics = MaterialCharacteristics(
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0
    ),

    val axisValues: List<Float> = materialCharacteristics.toListForDrawing(),
    val drawingStateStack: List<List<Float>> = listOf(axisValues),
    val selectedAxisValue: Float = 150f,
    val showAxisLabels: Boolean = false
) {

    fun isUndoButtonEnabled(): Boolean {
        return drawingStateStack.size > 1
    }
}

// https://stackoverflow.com/questions/929103/convert-a-number-range-to-another-range-maintaining-ratio
fun scaleToCharacteristics(
    value: Float,
    fromMin: Float = 0f,
    fromMax: Float = 300f,
    toMin: Double = AppConfig.PolarPlot.CHARACTERISTICS_MIN,
    toMax: Double = AppConfig.PolarPlot.CHARACTERISTICS_MAX)
: Double {
    return ((value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin)
}

fun scaleToDrawingFloats(
    value: Double,
    fromMin: Double = AppConfig.PolarPlot.CHARACTERISTICS_MIN,
    fromMax: Double = AppConfig.PolarPlot.CHARACTERISTICS_MAX,
    toMin: Float = 0f,
    toMax: Float = 300f)
: Float {
    return ((value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin).toFloat()
}

fun fromListForDrawingToMaterialCharacteristics(list: List<Float>): MaterialCharacteristics {
    return MaterialCharacteristics(
        checkeredPattern = scaleToCharacteristics(list[0]),
        surfaceRoughness = scaleToCharacteristics(list[1]),
        scaleOfPattern = scaleToCharacteristics(list[2]),
        multicolored = scaleToCharacteristics(list[3]),
        colorVibrancy = scaleToCharacteristics(list[4]),
        brightness = scaleToCharacteristics(list[5]),
        naturalness = scaleToCharacteristics(list[6]),
        value = scaleToCharacteristics(list[7]),
        warmth = scaleToCharacteristics(list[8]),
        thickness = scaleToCharacteristics(list[9]),
        hardness = scaleToCharacteristics(list[10]),
        movementEffect = scaleToCharacteristics(list[11]),
        shininess = scaleToCharacteristics(list[12]),
        sparkle = scaleToCharacteristics(list[13]),
        patternComplexity = scaleToCharacteristics(list[14]),
        stripedPattern = scaleToCharacteristics(list[15])
    )
}