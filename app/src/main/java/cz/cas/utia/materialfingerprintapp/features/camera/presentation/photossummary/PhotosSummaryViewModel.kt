package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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
            is PhotosSummaryEvent.SelectCategory -> TODO()
            is PhotosSummaryEvent.SetName -> TODO()
            PhotosSummaryEvent.SwitchLightDirections -> TODO()
        }
    }

    private fun goBackToCameraScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(PhotosSummaryNavigationEvent.BackToCameraScreen)
        }
    }
}