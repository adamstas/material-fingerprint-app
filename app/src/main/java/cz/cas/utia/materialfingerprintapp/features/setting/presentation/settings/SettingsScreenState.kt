package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import androidx.compose.runtime.Composable

data class SettingsScreenState(
    val isStoreDataOnServerSwitchChecked: Boolean = false,
    val selectedDefaultScreen: DefaultScreen = DefaultScreen.SETTINGS,

    val isDefaultScreenDropdownMenuExpanded: Boolean = false
)

enum class DefaultScreen {
    CAPTURING,
    ANALYSIS,
    SETTINGS
}

data class SettingsItemData(
    val text: String,
    val content: @Composable () -> Unit
)