package cz.cas.utia.materialfingerprintapp.features.capturing.presentation.photossummary

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory

sealed interface PhotosSummaryEvent {
    data class SetName(val name: String): PhotosSummaryEvent
    data class SelectCategory(val category: MaterialCategory): PhotosSummaryEvent
    data object SwitchLightDirections: PhotosSummaryEvent
    data object AnalyseImages: PhotosSummaryEvent

    data object GoBackToCameraScreen: PhotosSummaryEvent
    data object GoBackFromErrorScreen: PhotosSummaryEvent

    data object ShowDropdownMenu: PhotosSummaryEvent
    data object CloseDropdownMenu: PhotosSummaryEvent

    data object LoadImages: PhotosSummaryEvent
}

sealed interface PhotosSummaryNavigationEvent {
    data object BackToCameraScreen: PhotosSummaryNavigationEvent
    data class ToPolarPlotVisualisationScreen(
        val firstMaterialId: Long,
        val firstMaterialName: String
    ): PhotosSummaryNavigationEvent
}