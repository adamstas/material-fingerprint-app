package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.setting.data.SettingsRepository
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.DefaultScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainGraphViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private val _defaultStartDestination = MutableStateFlow<Screen?>(null)
    val defaultStartDestination: StateFlow<Screen?> = _defaultStartDestination.asStateFlow()

    private fun mapDefaultScreenToScreen(defaultScreen: DefaultScreen): Screen {
        return when (defaultScreen) {
            DefaultScreen.CAMERA -> Screen.Camera
            DefaultScreen.ANALYTICS -> Screen.AnalyticsHome
            DefaultScreen.SETTINGS -> Screen.Settings
        }
    }

    init {
        viewModelScope.launch {
            val defaultScreen = settingsRepository.getDefaultScreen()
            _defaultStartDestination.value = mapDefaultScreenToScreen(defaultScreen)
        }
    }
}