package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.home

sealed interface AnalysisHomeEvent {
    data object BrowseLocalMaterials: AnalysisHomeEvent
    data object BrowseRemoteMaterials: AnalysisHomeEvent
    data object SearchForMaterialsBasedOnTheirFingerprint: AnalysisHomeEvent
}

sealed interface AnalysisHomeNavigationEvent {
    data object ToBrowseLocalMaterialsScreen: AnalysisHomeNavigationEvent
    data object ToBrowseRemoteMaterialsScreen: AnalysisHomeNavigationEvent
    data object ToApplyFilterScreen: AnalysisHomeNavigationEvent
}