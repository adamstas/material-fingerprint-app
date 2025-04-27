package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.visualise

data class PolarPlotVisualisationScreenState(
    val plotDisplayMode: PlotDisplayMode = PlotDisplayMode.SINGLE_PLOT,

    val axisValuesFirst: List<Float> = List(16) { 150f },
    val axisValuesSecond: List<Float>? = null,

    val showAxisLabels: Boolean = true,

    val isFindSimilarMaterialsDialogShown: Boolean = false,

    val firstMaterialName: String = "",
    val secondMaterialName: String? = null
) {
    fun areBottomButtonsEnabled(): Boolean {
        return axisValuesSecond == null
    }

    fun isSegmentedButtonEnabled(): Boolean {
        return axisValuesSecond != null
    }
}

enum class PlotDisplayMode {
    SINGLE_PLOT,
    TWO_PLOTS
}