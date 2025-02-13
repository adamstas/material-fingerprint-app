package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalyticsHomeViewModel @Inject constructor(): ViewModel() {

    private val _state = MutableStateFlow(AnalyticsHomeScreenState)
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<AnalyticsHomeNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onEvent(event: AnalyticsHomeEvent) {
        when(event) {
            AnalyticsHomeEvent.BrowseLocalMaterials -> goToBrowseLocalMaterialsScreen()
            AnalyticsHomeEvent.BrowseRemoteMaterials -> goToBrowseRemoteMaterialsScreen()
            AnalyticsHomeEvent.SearchForMaterialsBasedOnTheirFingerprint -> goToApplyFilterScreen()
        }
    }

    private fun goToBrowseLocalMaterialsScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(AnalyticsHomeNavigationEvent.ToBrowseLocalMaterialsScreen)
        }
    }

    private fun goToBrowseRemoteMaterialsScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(AnalyticsHomeNavigationEvent.ToBrowseRemoteMaterialsScreen)
        }
    }

    private fun goToApplyFilterScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(AnalyticsHomeNavigationEvent.ToApplyFilterScreen)
        }
    }
}