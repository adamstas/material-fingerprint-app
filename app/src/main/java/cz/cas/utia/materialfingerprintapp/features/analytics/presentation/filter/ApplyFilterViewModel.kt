package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import cz.cas.utia.materialfingerprintapp.core.navigation.Screen
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsStorageSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApplyFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val materialCharacteristicsRepository: MaterialCharacteristicsRepository
): ViewModel() {

    private val _state = MutableStateFlow(ApplyFilterScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<ApplyFilterNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    init {
        val args = savedStateHandle.toRoute<Screen.ApplyFilter>()

        viewModelScope.launch {
            if (args.loadCharacteristicsFromStorage) {
                val characteristics = materialCharacteristicsRepository.loadMaterialCharacteristics(slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN)

                _state.update {
                    it.copy(
                        axisValues = characteristics.toListForDrawing(),
                        drawingStateStack = listOf(characteristics.toListForDrawing())
                    )
                }
            }
        }
    }

    private fun storeMaterialCharacteristics() {
        val materialCharacteristics = fromListForDrawingToMaterialCharacteristics(_state.value.axisValues)

        viewModelScope.launch {
            materialCharacteristicsRepository.saveMaterialCharacteristics(
                materialCharacteristics = materialCharacteristics,
                slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
            )
        }
    }

    private fun navigateToBrowseSimilarLocalMaterialsScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(ApplyFilterNavigationEvent.ToBrowseSimilarLocalMaterialsScreen)
        }
    }

    private fun navigateToBrowseSimilarRemoteMaterialsScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(ApplyFilterNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen)
        }
    }

    fun onEvent(event: ApplyFilterEvent) {
        when(event) {
            ApplyFilterEvent.ApplyOnLocalData -> applyOnLocalData()
            ApplyFilterEvent.ApplyOnServerData -> applyOnRemoteData()
            is ApplyFilterEvent.AddDrawingStateToStack -> addDrawingStateToStack(event)
            is ApplyFilterEvent.SetAxisValue -> setAxisValue(event)
            is ApplyFilterEvent.SetSelectedAxisValue -> setSelectedAxisValue(event)
            ApplyFilterEvent.ShowOrHideAxesLabels -> showOrHideAxesLabels()
            ApplyFilterEvent.UndoDrawingState -> undoDrawingState()
            ApplyFilterEvent.GoBackToAnalyticsHomeScreen -> goBackToAnalyticsHomeScreen()
        }
    }

    private fun goBackToAnalyticsHomeScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(ApplyFilterNavigationEvent.BackToAnalyticsHomeScreen)
        }
    }

    private fun applyOnLocalData() {
        storeMaterialCharacteristics()
        navigateToBrowseSimilarLocalMaterialsScreen()
    }

    private fun applyOnRemoteData() {
        storeMaterialCharacteristics()
        navigateToBrowseSimilarRemoteMaterialsScreen()
    }

    private fun addDrawingStateToStack(event: ApplyFilterEvent.AddDrawingStateToStack) {
        val mutableDrawingStateStack = _state.value.drawingStateStack.toMutableList()
        mutableDrawingStateStack.add(event.drawingState)

        _state.update {
            it.copy(
                drawingStateStack = mutableDrawingStateStack
            )
        }
    }

    private fun setAxisValue(event: ApplyFilterEvent.SetAxisValue) {
        val mutableAxisValuesList = _state.value.axisValues.toMutableList()
        mutableAxisValuesList[event.axisId] = event.value

        _state.update {
            it.copy(
                axisValues = mutableAxisValuesList
            )
        }
    }

    private fun setSelectedAxisValue(event: ApplyFilterEvent.SetSelectedAxisValue) {
        _state.update {
            it.copy(
                selectedAxisValue = event.value
            )
        }
    }

    private fun showOrHideAxesLabels() {
        _state.update {
            it.copy(
                showAxisLabels = !_state.value.showAxisLabels
            )
        }
    }

    private fun undoDrawingState() {
        if (_state.value.drawingStateStack.size > 1) {
            val mutableDrawingStateStack = _state.value.drawingStateStack.toMutableList()
            mutableDrawingStateStack.removeLast()

            val newDrawingState = mutableDrawingStateStack.last()
            val newAxisValues: MutableList<Float> = MutableList(16) { 0f }

            for (i in newDrawingState.indices) {
                newAxisValues[i] = newDrawingState[i]
            }

            _state.update {
                it.copy(
                    drawingStateStack = mutableDrawingStateStack,
                    axisValues = newAxisValues
                )
            }
        }
    }
}