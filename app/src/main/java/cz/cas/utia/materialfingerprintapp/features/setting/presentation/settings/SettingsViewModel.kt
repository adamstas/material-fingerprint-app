package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.setting.data.SettingsRepository
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
    private val settingsRepository: SettingsRepository
): ViewModel() {
    private val _state = MutableStateFlow(SettingsScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<SettingsNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private fun loadSettings(){
        viewModelScope.launch {
            val defaultScreen = settingsRepository.getDefaultScreen()
            val storeDataOnServerChoice = settingsRepository.getStoreDataOnServerChoice()

            _state.update {
                it.copy(
                    selectedDefaultScreen = defaultScreen,
                    isStoreDataOnServerSwitchChecked = storeDataOnServerChoice
                )
            }
        }
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
}