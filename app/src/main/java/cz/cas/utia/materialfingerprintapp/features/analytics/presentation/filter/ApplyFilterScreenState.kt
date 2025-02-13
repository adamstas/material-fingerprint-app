package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter

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

fun getAxisName(axisId: Int) = "Axis ${axisId + 1}"

// https://stackoverflow.com/questions/929103/convert-a-number-range-to-another-range-maintaining-ratio
fun scaleToCharacteristics(value: Float, fromMin: Float = 0f, fromMax: Float = 300f, toMin: Double = -2.75, toMax: Double = 2.75): Double {
    return ((value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin)
}

fun scaleToDrawingFloats(value: Double, fromMin: Double = -2.75, fromMax: Double = 2.75, toMin: Float = 0f, toMax: Float = 300f): Float {
    return ((value - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin).toFloat()
}

const val axesAmount = 16 // todo dat do nejakeho configu mimo

enum class MaterialCharacteristicsAttribute { // todo pouzit az budu mapovat idcka os na jejich popisky?
    BRIGHTNESS,
    COLOR_VIBRANCY,
    HARDNESS,
    CHECKERED_PATTERN,
    MOVEMENT_EFFECT,
    MULTICOLORED,
    NATURALNESS,
    PATTERN_COMPLEXITY,
    SCALE_OF_PATTERN,
    SHININESS,
    SPARKLE,
    STRIPED_PATTERN,
    SURFACE_ROUGHNESS,
    THICKNESS,
    VALUE,
    WARMTH;
}