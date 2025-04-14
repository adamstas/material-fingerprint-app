package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.visualise

sealed interface PolarPlotVisualisationEvent {
    data object FindSimilarMaterials: PolarPlotVisualisationEvent
    data object FindSimilarLocalMaterials: PolarPlotVisualisationEvent
    data object FindSimilarRemoteMaterials: PolarPlotVisualisationEvent

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