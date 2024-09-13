package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.MaterialUIElement

sealed interface MaterialEvent {
    //data object SaveMaterial: MaterialEvent
    data class SetName(val name: String): MaterialEvent //todo set name a set server id asi dat do camera feature
    data class SetServerId(val serverId: Int): MaterialEvent

    //todo what more to add?

    //todo pokud budou tyhle funkce pouzity i v Camera feature, tak to mozna hodit do Core nebo promyslet, co s tim (treba to bude OK)

    data class FilterMaterialsByCategory(val categories: List<MaterialCategory>): MaterialEvent

    data class CheckOrUncheckMaterial(val materialUIElement: MaterialUIElement): MaterialEvent

    //todo search
}