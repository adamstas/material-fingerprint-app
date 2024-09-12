package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

sealed interface MaterialEvent {
    object SaveMaterial: MaterialEvent
    data class SetName(val name: String)
    data class SetServerId(val serverId: Int)

    //todo what more to add?

    //todo pokud budou tyhle funkce pouzity i v Camera feature, tak to mozna hodit do Core nebo promyslet, co s tim (treba to bude OK)
}