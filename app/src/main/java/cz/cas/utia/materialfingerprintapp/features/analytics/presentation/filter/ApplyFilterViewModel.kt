package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ApplyFilterViewModel @Inject constructor(

): ViewModel() {

    private val _state = MutableStateFlow(ApplyFilterScreenState())
    val state = _state.asStateFlow()

    fun onEvent(event: ApplyFilterEvent) {
        when(event) {
            ApplyFilterEvent.ApplyOnLocalData -> applyOnLocalData()
            ApplyFilterEvent.ApplyOnServerData -> applyOnRemoteData()
            is ApplyFilterEvent.AddDrawingStateToStack -> addDrawingStateToStack(event)
            is ApplyFilterEvent.SetAxisValue -> setAxisValue(event)
            is ApplyFilterEvent.SetSelectedAxisValue -> setSelectedAxisValue(event)
            ApplyFilterEvent.ShowOrHideAxesLabels -> showOrHideAxesLabels()
            ApplyFilterEvent.UndoDrawingState -> undoDrawingState()
        }
    }

    private fun applyOnLocalData() {

    }

    private fun applyOnRemoteData() {

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