package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.visualise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.cas.utia.materialfingerprintapp.core.navigation.Screen
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsStorageSlot
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter.fromListForDrawingToMaterialCharacteristics
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
                materialCharacteristicsRepository.loadMaterialCharacteristics(MaterialCharacteristicsStorageSlot.REMOTE_FIRST) //todo udelat ukladani z remote viewmodelu!
            }

            val secondMaterial = if (_args.isSecondMaterialSourceLocal != null) {
              if (_args.isSecondMaterialSourceLocal) {
                   localMaterialRepository.getMaterial(_args.secondMaterialId!!).characteristics
               } else {
                   materialCharacteristicsRepository.loadMaterialCharacteristics(MaterialCharacteristicsStorageSlot.REMOTE_SECOND)
              }
            } else null

            _state.update {
                it.copy(
                    axisValuesFirst = firstMaterial.toListForDrawing(),
                    axisValuesSecond = secondMaterial?.toListForDrawing()
                )
            }
        }
    }

    fun onEvent(event: PolarPlotVisualisationEvent) {
        when (event) {
            PolarPlotVisualisationEvent.ApplyFilter -> applyFilter()
            PolarPlotVisualisationEvent.FindSimilarMaterial -> findSimilarMaterial()
            PolarPlotVisualisationEvent.GoBackToBrowseMaterialsScreen -> goBackToBrowseMaterialsScreen()
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

    private fun goBackToBrowseMaterialsScreen() {

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
            _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToBrowseSimilarLocalMaterialsScreen(materialId = _args.firstMaterialId))
        }
    }

    private fun findSimilarRemoteMaterial() {
        viewModelScope.launch {
            _navigationEvents.emit(PolarPlotVisualisationNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen(materialId = _args.firstMaterialId)) // todo takhle to delat - i pres to, ze tenhle material je klidne remote a jeho 16 cahrakteristik si nacitam z data store, tak pri hledani podobneho materialu mezi remote materialy mu to ID normalne dat namísto abych mu dal -1 a on si to muse ltahat z data storu
        }
    }
}