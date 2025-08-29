package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import androidx.compose.runtime.Composable
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SettingsScreenState(
    val isStoreDataOnServerSwitchChecked: Boolean = false,
    val selectedDefaultScreen: DefaultScreen = DefaultScreen.SETTINGS,

    val isDefaultScreenDropdownMenuExpanded: Boolean = false,

    val materialCsvExportStatus: MaterialExportStatus = MaterialExportStatus.NOT_STARTED,
    val materialZipExportStatus: MaterialExportStatus = MaterialExportStatus.NOT_STARTED,

    val exportImagesReady: Boolean = false,
    val exportMaterialsReady: Boolean = false,

    val serverUrl: String = AppConfig.Server.DEFAULT_URL,
    val isServerUrlValid: Boolean = true
)

enum class MaterialExportStatus {
    NOT_STARTED,
    IN_PROGRESS,
    FINISHED,
    NOTHING_TO_EXPORT
}

enum class DefaultScreen {
    CAPTURING,
    ANALYSIS,
    SETTINGS
}

data class SettingsItemData(
    val text: String,
    val content: @Composable () -> Unit
)

fun getCurrentDateAndTime(): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")
    return LocalDateTime.now().format(formatter)
}

fun getCurrentMaterialsCsvExportFileName(): String {
    val timestamp = getCurrentDateAndTime()
    return "${AppConfig.MaterialExporting.FILE_BASENAME_CSV}_$timestamp.csv"
}

fun getCurrentMaterialsZipExportFileName(): String {
    val timestamp = getCurrentDateAndTime()
    return "${AppConfig.MaterialExporting.FILE_BASENAME_ZIP}_$timestamp.zip"
}