package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PhotosSummaryViewModel @Inject constructor(
    private val imageStorageService: ImageStorageService
): ViewModel() {

    private val _state = MutableStateFlow(PhotosSummaryScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<PhotosSummaryNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onEvent(event: PhotosSummaryEvent) {
        when (event) {
            PhotosSummaryEvent.GoBackToCameraScreen -> goBackToCameraScreen()
            is PhotosSummaryEvent.SelectCategory -> selectCategory(event)
            is PhotosSummaryEvent.SetName -> setName(event)
            PhotosSummaryEvent.SwitchLightDirections -> swapLightDirections()
            PhotosSummaryEvent.CloseDropdownMenu -> closeDropdownMenu()
            PhotosSummaryEvent.ShowDropdownMenu -> showDropdownMenu()
            PhotosSummaryEvent.LoadImages -> loadImages()
            PhotosSummaryEvent.AnalyseImages -> analyseImages()
        }
    }

    private fun goBackToCameraScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(PhotosSummaryNavigationEvent.BackToCameraScreen)
        }
    }

    private fun selectCategory(event: PhotosSummaryEvent.SelectCategory) {
        _state.update { it.copy(
            selectedCategory = event.category
        ) }
    }

    private fun setName(event: PhotosSummaryEvent.SetName) {
        _state.update { it.copy(
            materialName = event.name
        ) }
    }

    private fun swapLightDirections() {
        val tmp = _state.value.lightDirectionSlot1

        _state.update { it.copy(
            lightDirectionSlot1 = _state.value.lightDirectionSlot2,
            lightDirectionSlot2 = tmp
        ) }
    }

    private fun closeDropdownMenu() {
        _state.update { it.copy(
            isDropdownMenuExpanded = false
        ) }
    }

    private fun showDropdownMenu() {
        _state.update { it.copy(
            isDropdownMenuExpanded = true
        ) }
    }

    private fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageSlot1 = imageStorageService.loadImage("slot1")
            val imageSlot2 = imageStorageService.loadImage("slot2")

            withContext(Dispatchers.Main) {
                _state.update { it.copy(
                    capturedImageSlot1 = imageSlot1,
                    capturedImageSlot2 = imageSlot2
                ) }
            }
        }
    }

    private fun analyseImages() {
        // todo call service and until it returns data, show some loading circle animation..

        //todo later move this just after the app receives successful result from server
        imageStorageService.deleteImage("slot1")
        imageStorageService.deleteImage("slot2")

        _state.update { it.copy(
            capturedImageSlot1 = null,
            capturedImageSlot2 = null
        ) }
    }
}