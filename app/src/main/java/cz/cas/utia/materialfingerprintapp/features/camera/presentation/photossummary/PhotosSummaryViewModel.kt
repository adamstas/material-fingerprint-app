package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.RemoteMaterialRepository
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
    private val imageStorageService: ImageStorageService,
    private val remoteMaterialRepository: RemoteMaterialRepository,
    private val localMaterialRepository: LocalMaterialRepository
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
        _state.update { it.copy(
            isLoadingDialogShown = true
        ) }

        viewModelScope.launch {
            val material = remoteMaterialRepository.analyseMaterial(
                firstImageLightDirection = _state.value.lightDirectionSlot1,
                name = _state.value.materialName,
                category = _state.value.selectedCategory
            )

            val materialId = localMaterialRepository.insertMaterial(material)

            // todo ted ukladam specular (coz je zatim ten, na ktery se sviti zleva), pak se lze dohodnout, jaky budeme ukladat
            val imageToStore = if (_state.value.lightDirectionSlot1 == LightDirection.FROM_LEFT) _state.value.capturedImageSlot1 else _state.value.capturedImageSlot2
            imageStorageService.storeImage(imageToStore!!, materialId.toString())

            imageStorageService.deleteImage("slot1")
            imageStorageService.deleteImage("slot2")

            _state.update { it.copy(
                capturedImageSlot1 = null,
                capturedImageSlot2 = null,
                isLoadingDialogShown = false
            ) }

            _navigationEvents.emit(PhotosSummaryNavigationEvent.ToPolarPlotVisualisationScreen(
                    firstMaterialId = materialId,
                    firstMaterialName = _state.value.materialName
            ))
        }
    }
}