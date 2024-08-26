package cz.cas.utia.materialfingerprintapp.features.setting.presentation

import cz.cas.utia.materialfingerprintapp.R

//todo presunout z presentation package (nebo to tu nechat, pokud tu budou data jen pro UI)

sealed class Permissions(val title: String, val description: String, val iconId: Int) {
    data object Camera: Permissions( //CAMERA
        title = "Permission to use camera",
        description = "The app needs to use your camera to take photos of materials ...",
        iconId = R.drawable.photo_camera_filled
    )

    data object ReadFiles: Permissions( //READ_EXTERNAL_STORAGE
        title = "Permission to read files", //todo pokud tohle permission puzijeme, najit spravnou ikonu
        description = "To be able to use your previously taken photos the app needs to the permission to read files in your device...",
        iconId = R.drawable.analytics_filled
    )
}

val permissions = listOf(
    Permissions.Camera,
    Permissions.ReadFiles
)