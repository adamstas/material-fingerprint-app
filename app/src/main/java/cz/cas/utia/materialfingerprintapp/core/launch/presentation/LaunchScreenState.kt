package cz.cas.utia.materialfingerprintapp.core.launch.presentation

sealed interface LaunchScreenState {
    data object Loading: LaunchScreenState
    data object ShowTutorial: LaunchScreenState
    data object ShowPermissionsScreen: LaunchScreenState
    data object LaunchMainContent: LaunchScreenState
}