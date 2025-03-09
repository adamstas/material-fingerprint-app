package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter

import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics

data class ApplyFilterScreenState( //todo fakt jsou materialy od zhruba -2,75 do +2,75 ?
    val materialCharacteristics: MaterialCharacteristics = MaterialCharacteristics( //todo az budu mit realne hodnoty, tak vytvorit par defaultnich (drevo, latka, ...) a jeden z nich tady nastavit a ve state mit jen ten list<float> axisValues
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
        0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,0.0
    ),

    val axisValues: List<Float> = materialCharacteristics.toListForDrawing(), //todo pozdeji ten list udelat z objektu co ziskam v data store a pracovat uz jen s tim listem
    val drawingStateStack: List<List<Float>> = listOf(axisValues),
    val selectedAxisValue: Float = 0f, //todo pak vymyslet co tam zobrazit na zacatku, asi to dat nullable..
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
    toMin: Double = AppConfig.PolarPlot.characteristicsMin,
    toMax: Double = AppConfig.PolarPlot.characteristicsMax)
: Double {
    return ((value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin)
}

fun scaleToDrawingFloats(
    value: Double,
    fromMin: Double = AppConfig.PolarPlot.characteristicsMin,
    fromMax: Double = AppConfig.PolarPlot.characteristicsMax,
    toMin: Float = 0f,
    toMax: Float = 300f)
: Float {
    return ((value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin).toFloat()
}

// todo osetrit zda je velikost presne axesAMount, jinak hodit exception..?
fun fromListForDrawingToMaterialCharacteristics(list: List<Float>): MaterialCharacteristics {
    return MaterialCharacteristics(
        brightness = scaleToCharacteristics(list[0]),
        colorVibrancy = scaleToCharacteristics(list[1]),
        hardness = scaleToCharacteristics(list[2]),
        checkeredPattern = scaleToCharacteristics(list[3]),
        movementEffect = scaleToCharacteristics(list[4]),
        multicolored = scaleToCharacteristics(list[5]),
        naturalness = scaleToCharacteristics(list[6]),
        patternComplexity = scaleToCharacteristics(list[7]),
        scaleOfPattern = scaleToCharacteristics(list[8]),
        shininess = scaleToCharacteristics(list[9]),
        sparkle = scaleToCharacteristics(list[10]),
        stripedPattern = scaleToCharacteristics(list[11]),
        surfaceRoughness = scaleToCharacteristics(list[12]),
        thickness = scaleToCharacteristics(list[13]),
        value = scaleToCharacteristics(list[14]),
        warmth = scaleToCharacteristics(list[15])
    )
}