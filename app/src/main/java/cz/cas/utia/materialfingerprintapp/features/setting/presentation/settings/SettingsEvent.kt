package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

sealed interface SettingsEvent {
    data class SwitchStoreDataOnServerSwitch(val newSwitchValue: Boolean): SettingsEvent
    data class SelectDefaultScreen(val selected: DefaultScreen): SettingsEvent

    data object ShowDropdownMenu: SettingsEvent
    data object CloseDropdownMenu: SettingsEvent

    data object ReplayTutorial: SettingsEvent
}

sealed interface SettingsNavigationEvent {
    data object ToTutorialScreen: SettingsNavigationEvent
}