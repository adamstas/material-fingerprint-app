package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import android.net.Uri

sealed interface SettingsEvent {
    data class SwitchStoreDataOnServerSwitch(val newSwitchValue: Boolean): SettingsEvent
    data class SelectDefaultScreen(val selected: DefaultScreen): SettingsEvent

    data object ShowDropdownMenu: SettingsEvent
    data object CloseDropdownMenu: SettingsEvent

    data object ReplayTutorial: SettingsEvent

    data object CheckIfAnyImagesToExport: SettingsEvent
    data object CheckIfAnyMaterialsToExport: SettingsEvent

    data class ExportLocalMaterialsAsCsv(val uri: Uri): SettingsEvent
    data class ExportLocalMaterialImagesAsZip(val uri: Uri): SettingsEvent

    data object SetCsvExportStatusAsNotStarted: SettingsEvent
    data object SetZipExportStatusAsNotStarted: SettingsEvent

    data class SetServerUrl(val url: String): SettingsEvent
}

sealed interface SettingsNavigationEvent {
    data object ToTutorialScreen: SettingsNavigationEvent
}