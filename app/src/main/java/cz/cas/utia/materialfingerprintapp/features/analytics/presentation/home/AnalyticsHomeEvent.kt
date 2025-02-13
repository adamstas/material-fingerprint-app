package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.home

sealed interface AnalyticsHomeEvent {
    data object BrowseLocalMaterials: AnalyticsHomeEvent
    data object BrowseRemoteMaterials: AnalyticsHomeEvent
    data object SearchForMaterialsBasedOnTheirFingerprint: AnalyticsHomeEvent
}

sealed interface AnalyticsHomeNavigationEvent {
    data object ToBrowseLocalMaterialsScreen: AnalyticsHomeNavigationEvent
    data object ToBrowseRemoteMaterialsScreen: AnalyticsHomeNavigationEvent
    data object ToApplyFilterScreen: AnalyticsHomeNavigationEvent
}