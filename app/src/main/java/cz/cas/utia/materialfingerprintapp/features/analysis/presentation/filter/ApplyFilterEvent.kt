package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.filter

sealed interface ApplyFilterEvent {
    data class AddDrawingStateToStack(val drawingState: List<Float>): ApplyFilterEvent

    data class SetAxisValue(val axisId: Int, val value: Float): ApplyFilterEvent
    data class SetSelectedAxisValue(val value: Float): ApplyFilterEvent

    data object ShowOrHideAxesLabels: ApplyFilterEvent
    data object UndoDrawingState: ApplyFilterEvent

    data object ApplyOnLocalData: ApplyFilterEvent
    data object ApplyOnServerData: ApplyFilterEvent

    data object GoBackToAnalyticsHomeScreen: ApplyFilterEvent
}

sealed interface ApplyFilterNavigationEvent {
    data object BackToAnalyticsHomeScreen: ApplyFilterNavigationEvent
    data object ToBrowseSimilarLocalMaterialsScreen: ApplyFilterNavigationEvent
    data object ToBrowseSimilarRemoteMaterialsScreen: ApplyFilterNavigationEvent
}