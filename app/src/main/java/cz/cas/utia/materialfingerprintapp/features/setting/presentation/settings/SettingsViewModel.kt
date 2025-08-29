package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.setting.domain.MaterialExportService
import cz.cas.utia.materialfingerprintapp.features.setting.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val localMaterialRepository: LocalMaterialRepository,
    private val materialExportService: MaterialExportService
): ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<SettingsNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private fun loadSettings(){
        viewModelScope.launch {
            val defaultScreen = settingsRepository.getDefaultScreen()
            val storeDataOnServerChoice = settingsRepository.getStoreDataOnServerChoice()
            val serverUrl = settingsRepository.getServerUrl()

            _state.update {
                it.copy(
                    selectedDefaultScreen = defaultScreen,
                    isStoreDataOnServerSwitchChecked = storeDataOnServerChoice,
                    serverUrl = serverUrl
                )
            }
        }
    }

    // validates URL if it is valid IP address or domain
    private fun isUrlValid(url: String): Boolean {
        val regex = "^https?://(" +
                "([a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}" + // domains
                "|" +
                "((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])\\.){3}" + // IP: first 3 octets
                "(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])" +           // IP: the last octet
                ")" +
                "(:[0-9]+)?/?\$"
        return regex.toRegex().matches(url)
    }

    init {
        loadSettings()
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            SettingsEvent.CloseDropdownMenu -> closeDropdownMenu()
            SettingsEvent.ShowDropdownMenu -> showDropdownMenu()
            is SettingsEvent.SelectDefaultScreen -> selectDefaultScreen(event)
            is SettingsEvent.SwitchStoreDataOnServerSwitch -> switchStoreDataOnServerSwitch(event)
            SettingsEvent.ReplayTutorial -> replayTutorial()
            is SettingsEvent.ExportLocalMaterialsAsCsv -> exportLocalMaterialsAsCsv(event)
            is SettingsEvent.ExportLocalMaterialImagesAsZip -> exportLocalMaterialImagesAsZip(event)
            SettingsEvent.SetCsvExportStatusAsNotStarted -> setCsvExportStatusAsNotStarted()
            is SettingsEvent.SetServerUrl -> setServerUrl(event)
            SettingsEvent.SetZipExportStatusAsNotStarted -> setZipExportStatusAsNotStarted()
            SettingsEvent.CheckIfAnyImagesToExport -> checkIfAnyImagesToExport()
            SettingsEvent.CheckIfAnyMaterialsToExport -> checkIfAnyMaterialsToExport()
        }
    }

    private fun closeDropdownMenu() {
        _state.update {
            it.copy(
                isDefaultScreenDropdownMenuExpanded = false
            )
        }
    }

    private fun showDropdownMenu() {
        _state.update {
            it.copy(
                isDefaultScreenDropdownMenuExpanded = true
            )
        }
    }

    private fun selectDefaultScreen(event: SettingsEvent.SelectDefaultScreen) {
        viewModelScope.launch {
            settingsRepository.saveDefaultScreen(event.selected)
        }

        _state.update {
            it.copy(
                selectedDefaultScreen = event.selected
            )
        }
    }

    private fun switchStoreDataOnServerSwitch(event: SettingsEvent.SwitchStoreDataOnServerSwitch) {
        viewModelScope.launch {
            settingsRepository.saveStoreDataOnServerChoice(event.newSwitchValue)
        }

        _state.update {
            it.copy(
                isStoreDataOnServerSwitchChecked = event.newSwitchValue
            )
        }
    }

    private fun replayTutorial() {
        viewModelScope.launch {
            _navigationEvents.emit(SettingsNavigationEvent.ToTutorialScreen)
        }
    }

    private fun checkIfAnyImagesToExport() {
        val hasImages = materialExportService.checkIfAnyImagesToExport()

        _state.update {
            if (hasImages) {
                it.copy(
                    exportImagesReady = true,
                    materialZipExportStatus = MaterialExportStatus.IN_PROGRESS
                )
            } else {
                it.copy(
                    exportImagesReady = false,
                    materialZipExportStatus = MaterialExportStatus.NOTHING_TO_EXPORT
                )
            }
        }
    }

    private fun checkIfAnyMaterialsToExport() {
        viewModelScope.launch {
            val materials = localMaterialRepository.getAllMaterialsOrderedByName()

            _state.update {
                if (materials.isNotEmpty()) {
                    it.copy(
                        exportMaterialsReady = true,
                        materialCsvExportStatus = MaterialExportStatus.IN_PROGRESS
                    )
                } else {
                    it.copy(
                        exportMaterialsReady = false,
                        materialCsvExportStatus = MaterialExportStatus.NOTHING_TO_EXPORT
                    )
                }
            }
        }
    }

    private fun exportLocalMaterialsAsCsv(event: SettingsEvent.ExportLocalMaterialsAsCsv) {
        _state.update {
            it.copy(
                materialCsvExportStatus = MaterialExportStatus.IN_PROGRESS
            )
        }

        viewModelScope.launch {
            val materials = localMaterialRepository.getAllMaterialsOrderedByName()

            materialExportService.exportMaterialsAsCsv(
                uri = event.uri,
                materials = materials
            )
            _state.update {
                it.copy(
                    materialCsvExportStatus = MaterialExportStatus.FINISHED,
                    exportMaterialsReady = false
                )
            }
        }
    }

    private fun setCsvExportStatusAsNotStarted() {
        _state.update {
            it.copy(
                materialCsvExportStatus = MaterialExportStatus.NOT_STARTED,
                exportMaterialsReady = false
            )
        }
    }

    private fun setZipExportStatusAsNotStarted() {
        _state.update {
            it.copy(
                materialZipExportStatus = MaterialExportStatus.NOT_STARTED,
                exportImagesReady = false
            )
        }
    }

    private fun exportLocalMaterialImagesAsZip(event: SettingsEvent.ExportLocalMaterialImagesAsZip) {
        _state.update {
            it.copy(
                materialZipExportStatus = MaterialExportStatus.IN_PROGRESS
            )
        }

        viewModelScope.launch {
            materialExportService.exportAllLocalMaterialImagesAsZip(uri = event.uri)

            _state.update {
               it.copy(
                   materialZipExportStatus = MaterialExportStatus.FINISHED,
                   exportImagesReady = false
               )
            }
        }
    }

    private fun setServerUrl(event: SettingsEvent.SetServerUrl) {
        val isValid = isUrlValid(event.url)

        _state.update {
            it.copy(
                serverUrl = event.url,
                isServerUrlValid = isValid
            )
        }

        if (isValid) {
            viewModelScope.launch {
                settingsRepository.saveServerUrl(event.url)
            }
        }
    }
}