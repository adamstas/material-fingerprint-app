package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.visualise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.cas.utia.materialfingerprintapp.core.navigation.Screen
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsStorageSlot
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.filter.fromListForDrawingToMaterialCharacteristics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PolarPlotVisualisationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val materialCharacteristicsRepository: MaterialCharacteristicsRepository,
    private val localMaterialRepository: LocalMaterialRepository
): ViewModel() {

    private val _state = MutableStateFlow(PolarPlotVisualisationScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<PolarPlotVisualisationNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    private val _args = savedStateHandle.toRoute<Screen.PolarPlotVisualisation>()

    init {
        viewModelScope.launch {
            val firstMaterial: MaterialCharacteristics = if (_args.isFirstMaterialSourceLocal) {
                localMaterialRepository.getMaterial(_args.firstMaterialId).characteristics
            } else {
                materialCharacteristicsRepository.loadMaterialCharacteristics(
                    MaterialCharacteristicsStorageSlot.REMOTE_FIRST)
            }

            val secondMaterial = if (_args.isSecondMaterialSourceLocal != null) {
              if (_args.isSecondMaterialSourceLocal) {
                   localMaterialRepository.getMaterial(_args.secondMaterialId!!).characteristics
               } else {
                   materialCharacteristicsRepository.loadMaterialCharacteristics(
                       MaterialCharacteristicsStorageSlot.REMOTE_SECOND)
              }
            } else null

            _state.update {
                it.copy(
                    axisValuesFirst = firstMaterial.toListForDrawing(),
                    firstMaterialName = _args.firstMaterialName,
                    axisValuesSecond = secondMaterial?.toListForDrawing(),
                    secondMaterialName = _args.secondMaterialName
                )
            }
        }
    }

    fun onEvent(event: PolarPlotVisualisationEvent) {
        when (event) {
            PolarPlotVisualisationEvent.ApplyFilter -> applyFilter()
            PolarPlotVisualisationEvent.FindSimilarMaterial -> findSimilarMaterial()
            PolarPlotVisualisationEvent.GoBack -> goBack()
            PolarPlotVisualisationEvent.ShowOrHideAxesLabels -> showOrHideAxesLabels()
            is PolarPlotVisualisationEvent.SetPlotDisplayMode -> setPolarPlotDisplayMode(event)
            PolarPlotVisualisationEvent.DismissFindSimilarMaterialsDialog -> dismissFindSimilarMaterialsDialog()
            PolarPlotVisualisationEvent.FindSimilarLocalMaterial -> findSimilarLocalMaterial()
            PolarPlotVisualisationEvent.FindSimilarRemoteMaterial -> findSimilarRemoteMaterial()
        }
    }

    private fun applyFilter() {
        viewModelScope.launch {
            materialCharacteristicsRepository.saveMaterialCharacteristics(
                materialCharacteristics = fromListForDrawingToMaterialCharacteristics(_state.value.axisValuesFirst),
                slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
            )
            _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToApplyFilterScreen)
        }
    }

    private fun findSimilarMaterial() {
        _state.update {
            it.copy(
                isFindSimilarMaterialsDialogShown = true
            )
        }
    }

    private fun goBack() {
        viewModelScope.launch {
            _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.Back)
        }
    }

    private fun showOrHideAxesLabels() {
        _state.update {
            it.copy(
                showAxisLabels = !_state.value.showAxisLabels
            )
        }
    }

    private fun setPolarPlotDisplayMode(event: PolarPlotVisualisationEvent.SetPlotDisplayMode) {
        _state.update {
            it.copy(
                plotDisplayMode = event.plotDisplayMode
            )
        }
    }

    private fun dismissFindSimilarMaterialsDialog() {
        _state.update {
            it.copy(
                isFindSimilarMaterialsDialogShown = false
            )
        }
    }

    private fun findSimilarLocalMaterial() {
        viewModelScope.launch {
            if (_args.isFirstMaterialSourceLocal) { // material is local, therefore its ID is from local database and it can be passed to browse similar LOCAL materials screen
                _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToBrowseSimilarLocalMaterialsScreen(materialId = _args.firstMaterialId))

            } else { // material is remote, therefore its ID is from remote database and it cannot be passed to browse similar LOCAL materials screen => store it to data store and pass -1L as parameter
                materialCharacteristicsRepository.saveMaterialCharacteristics(
                    materialCharacteristics = fromListForDrawingToMaterialCharacteristics(_state.value.axisValuesFirst),
                    slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
                )
                _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToBrowseSimilarLocalMaterialsScreen(materialId = -1L))
            }
        }
    }

    private fun findSimilarRemoteMaterial() {
        viewModelScope.launch {

            if (_args.isFirstMaterialSourceLocal) { // material is local, therefore its ID is from local database and it cannot be passed to browse similar REMOTE materials screen
                materialCharacteristicsRepository.saveMaterialCharacteristics(
                    materialCharacteristics = fromListForDrawingToMaterialCharacteristics(_state.value.axisValuesFirst),
                    slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
                )
                _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen(materialId = -1L))

            } else { // material is remote, therefore its ID is from remote database and it can be passed to browse similar REMOTE materials screen
                _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen(materialId = _args.firstMaterialId))
            }
        }
    }
}