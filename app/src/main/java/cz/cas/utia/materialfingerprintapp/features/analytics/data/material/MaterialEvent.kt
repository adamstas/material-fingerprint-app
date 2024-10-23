package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

sealed interface MaterialEvent {
    //data object SaveMaterial: MaterialEvent
    data class SetName(val name: String): MaterialEvent //todo set name a set server id asi dat do camera feature
    data class SetServerId(val serverId: Int): MaterialEvent

    //todo what more to add?

    //todo pokud budou tyhle funkce pouzity i v Camera feature, tak to mozna hodit do Core nebo promyslet, co s tim (treba to bude OK)

    data class FilterMaterialsByCategory(val categoryIDs: List<Int>): MaterialEvent

    data class CheckMaterial(val materialID: Int): MaterialEvent
    data class UncheckMaterial(val materialID: Int): MaterialEvent

    data class CheckOrUncheckCategory(val categoryID: Int): MaterialEvent

    data class ShowOrHideDropdownMenu(val newState: Boolean): MaterialEvent
    data object CloseDropdownMenu: MaterialEvent

    //todo search
}