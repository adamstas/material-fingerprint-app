package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.visualise

// todo byt konzistentni a vsude pouzivat "Materials" (browse similar materials atd.)

sealed interface PolarPlotVisualisationEvent {
    data object FindSimilarMaterial: PolarPlotVisualisationEvent
    data object FindSimilarLocalMaterial: PolarPlotVisualisationEvent
    data object FindSimilarRemoteMaterial: PolarPlotVisualisationEvent

    data object ApplyFilter: PolarPlotVisualisationEvent

    data object GoBack: PolarPlotVisualisationEvent

    data object ShowOrHideAxesLabels: PolarPlotVisualisationEvent
    data class SetPlotDisplayMode(val plotDisplayMode: PlotDisplayMode): PolarPlotVisualisationEvent

    data object DismissFindSimilarMaterialsDialog: PolarPlotVisualisationEvent
}

sealed interface PolarPlotVisualisationNavigationEvent {
    data object Back: PolarPlotVisualisationNavigationEvent
    data class ToBrowseSimilarLocalMaterialsScreen(val materialId: Long): PolarPlotVisualisationNavigationEvent
    data class ToBrowseSimilarRemoteMaterialsScreen(val materialId: Long): PolarPlotVisualisationNavigationEvent
    data object ToApplyFilterScreen: PolarPlotVisualisationNavigationEvent
}