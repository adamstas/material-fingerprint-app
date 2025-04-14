package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary

sealed interface BrowseMaterialsEvent {
    data class CheckMaterial(val material: MaterialSummary): BrowseMaterialsEvent
    data class UncheckMaterial(val material: MaterialSummary): BrowseMaterialsEvent

    data class CheckOrUncheckCategory(val categoryID: Int): BrowseMaterialsEvent

    data object ShowDropdownMenu: BrowseMaterialsEvent
    data object CloseDropdownMenu: BrowseMaterialsEvent

    data class SearchMaterials(val searchedText: String): BrowseMaterialsEvent

    data class FindSimilarLocalMaterials(val material: MaterialSummary): BrowseMaterialsEvent
    data class FindSimilarRemoteMaterials(val material: MaterialSummary): BrowseMaterialsEvent
    data object FindSimilarMaterial: BrowseMaterialsEvent

    data object CreatePolarPlot: BrowseMaterialsEvent

    data object DismissFindSimilarMaterialsDialog: BrowseMaterialsEvent

    data object GoBack: BrowseMaterialsEvent
}

sealed interface BrowseMaterialsNavigationEvent {
    data class ToBrowseSimilarLocalMaterialsScreen(val materialID: Long): BrowseMaterialsNavigationEvent
    data class ToBrowseSimilarRemoteMaterialsScreen(val materialID: Long): BrowseMaterialsNavigationEvent
    data class ToPolarPlotVisualisationScreen(
        val isFirstMaterialSourceLocal: Boolean,
        val firstMaterialId: Long,
        val firstMaterialName: String,
        val isSecondMaterialSourceLocal: Boolean?,
        val secondMaterialId: Long?,
        val secondMaterialName: String?
    ): BrowseMaterialsNavigationEvent
    data object Back: BrowseMaterialsNavigationEvent
}