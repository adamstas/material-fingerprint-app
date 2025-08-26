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
            SettingsEvent.ExportLocalMaterialsAsZip -> exportLocalMaterialsAsZip()
            SettingsEvent.SetExportStatusAsNotStarted -> setExportStatusAsNotStarted()
            is SettingsEvent.SetServerUrl -> setServerUrl(event)
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

    private fun exportLocalMaterialsAsCsv(event: SettingsEvent.ExportLocalMaterialsAsCsv) {
        _state.update {
            it.copy(
                materialExportStatus = MaterialExportStatus.IN_PROGRESS
            )
        }

        viewModelScope.launch {
            val materials = localMaterialRepository.getAllMaterialsOrderedByName()
            materialExportService.exportMaterials(
                uri = event.uri,
                materials = materials
            )
            _state.update {
                it.copy(
                    materialExportStatus = MaterialExportStatus.FINISHED
                )
            }
        }
    }

    private fun setExportStatusAsNotStarted() {
        _state.update {
            it.copy(
                materialExportStatus = MaterialExportStatus.NOT_STARTED
            )
        }
    }

    private fun exportLocalMaterialsAsZip() {

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