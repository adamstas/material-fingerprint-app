package cz.cas.utia.materialfingerprintapp.features.setting.presentation.permission

sealed interface PermissionsEvent {
    data class OnPermissionResult(val permission: String, val isGranted: Boolean): PermissionsEvent
    data class ShowPermissionDialog(val permission: String): PermissionsEvent
    data object DismissPermissionsDialog: PermissionsEvent
}