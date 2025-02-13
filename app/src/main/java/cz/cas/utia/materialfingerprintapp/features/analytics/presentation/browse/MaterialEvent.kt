package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

sealed interface MaterialEvent {
    //data object SaveMaterial: MaterialEvent
    data class SetName(val name: String): MaterialEvent //todo set name a set server id asi dat do camera feature
    data class SetServerId(val serverId: Int): MaterialEvent

    //todo pokud budou tyhle funkce pouzity i v Camera feature, tak to mozna hodit do Core nebo promyslet, co s tim (treba to bude OK)

    data class CheckMaterial(val materialID: Long): MaterialEvent
    data class UncheckMaterial(val materialID: Long): MaterialEvent

    data class CheckOrUncheckCategory(val categoryID: Int): MaterialEvent

    data object ShowDropdownMenu: MaterialEvent
    data object CloseDropdownMenu: MaterialEvent

    data class SearchMaterials(val searchedText: String): MaterialEvent

    data class FindSimilarLocalMaterials(val materialID: Long): MaterialEvent
    data class FindSimilarRemoteMaterials(val materialID: Long): MaterialEvent
}

sealed interface MaterialNavigationEvent { //todo nehcat to takhle "MaterialNavigationEvent"?
    data class ToBrowseSimilarLocalMaterialsScreen(val materialID: Long): MaterialNavigationEvent
    data class ToBrowseSimilarRemoteMaterialsScreen(val materialID: Long): MaterialNavigationEvent
}