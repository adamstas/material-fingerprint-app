package cz.cas.utia.materialfingerprintapp.features.setting.presentation.permission

import android.Manifest
import cz.cas.utia.materialfingerprintapp.R

data class PermissionsScreenState (
    val permissions: List<PermissionStatus> = emptyList(),

    val visiblePermissionInDialog: String = ""
) {

    fun allPermissionsGranted(): Boolean {
        return permissions.isNotEmpty() && permissions.all { it.isGranted }
    }
}

data class PermissionStatus(
    val permission: String,
    val isGranted: Boolean
)

val requiredPermissions = listOf(
    Manifest.permission.CAMERA
)

fun getPermissionTitle(permission: String): String {
    return when (permission) {
        Manifest.permission.CAMERA -> "Permission to use camera"
        else -> "PERMISSION TITLE"
    }
}

fun getPermissionDescription(permission: String): String {
    return when (permission) {
        Manifest.permission.CAMERA -> "This app needs access to your camera so that you can take pictures " +
                "of materials and analyse them."
        else -> "PERMISSION DESCRIPTION"
    }
}

fun getPermissionIcon(permission: String): Int {
    return when (permission) {
        Manifest.permission.CAMERA -> R.drawable.photo_camera_filled
        else -> R.drawable.photo_camera_filled
    }
}