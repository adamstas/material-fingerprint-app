package cz.cas.utia.materialfingerprintapp.core.launch.presentation

interface LaunchEvent {
    data object TutorialCompleted: LaunchEvent
    data object PermissionsGranted: LaunchEvent
}