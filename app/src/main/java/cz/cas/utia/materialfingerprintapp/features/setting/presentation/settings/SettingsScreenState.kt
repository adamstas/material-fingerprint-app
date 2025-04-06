package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import androidx.compose.runtime.Composable

data class SettingsScreenState(
    val isSendDataToServerSwitchChecked: Boolean = false,
    val selectedDefaultScreen: DefaultScreen = DefaultScreen.CAMERA,

    val isDefaultScreenDropdownMenuExpanded: Boolean = false
)

enum class DefaultScreen {
    CAMERA,
    ANALYTICS,
    SETTINGS
}

data class SettingsItemData(
    val text: String,
    val content: @Composable () -> Unit
)