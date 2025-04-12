package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnalysisHomeViewModel @Inject constructor(): ViewModel() {

    private val _navigationEvents = MutableSharedFlow<AnalysisHomeNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onEvent(event: AnalysisHomeEvent) {
        when(event) {
            AnalysisHomeEvent.BrowseLocalMaterials -> goToBrowseLocalMaterialsScreen()
            AnalysisHomeEvent.BrowseRemoteMaterials -> goToBrowseRemoteMaterialsScreen()
            AnalysisHomeEvent.SearchForMaterialsBasedOnTheirFingerprint -> goToApplyFilterScreen()
        }
    }

    private fun goToBrowseLocalMaterialsScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(AnalysisHomeNavigationEvent.ToBrowseLocalMaterialsScreen)
        }
    }

    private fun goToBrowseRemoteMaterialsScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(AnalysisHomeNavigationEvent.ToBrowseRemoteMaterialsScreen)
        }
    }

    private fun goToApplyFilterScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(AnalysisHomeNavigationEvent.ToApplyFilterScreen)
        }
    }
}