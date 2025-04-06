package cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.api.exception.NoInternetException
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.RemoteMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import cz.cas.utia.materialfingerprintapp.features.setting.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PhotosSummaryViewModel @Inject constructor(
    private val imageStorageService: ImageStorageService,
    private val remoteMaterialRepository: RemoteMaterialRepository,
    private val localMaterialRepository: LocalMaterialRepository,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private val _state = MutableStateFlow<PhotosSummaryScreenState>(PhotosSummaryScreenState.Success())
    val state = _state.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<PhotosSummaryNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    // helper for less boiler plate code
    private fun updateSuccessState(update: (PhotosSummaryScreenState.Success) -> PhotosSummaryScreenState) {
        val currentState = _state.value
        if (currentState is PhotosSummaryScreenState.Success) {
            _state.value = update(currentState)
        }
    }

    // for error recovery
    private var _lastState = PhotosSummaryScreenState.Success()

    private fun storeCurrentState() {
        when (val currentState = _state.value) {
            is PhotosSummaryScreenState.Success -> {
                _lastState = currentState.copy(
                    isDropdownMenuExpanded = false,
                    isLoadingDialogShown = false
                )
            }
            is PhotosSummaryScreenState.Error -> {} // do not store Error state
        }
    }

    private fun resetStateAfterError() {
        _state.value = _lastState
    }

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
            PhotosSummaryEvent.GoBackFromErrorScreen -> goBackFromErrorScreen()
        }
    }

    private fun goBackToCameraScreen() {
        viewModelScope.launch {
            _navigationEvents.emit(PhotosSummaryNavigationEvent.BackToCameraScreen)
        }
    }

    private fun selectCategory(event: PhotosSummaryEvent.SelectCategory) {
        updateSuccessState { it.copy(selectedCategory = event.category) }
    }

    private fun setName(event: PhotosSummaryEvent.SetName) {
        updateSuccessState { it.copy(materialName = event.name) }
    }

    private fun swapLightDirections() {
        _state.value.takeIf { it is PhotosSummaryScreenState.Success }?.let {
            val successState = it as PhotosSummaryScreenState.Success

            val tmp = successState.lightDirectionSlot1
            _state.value = successState.copy(
                lightDirectionSlot1 = successState.lightDirectionSlot2,
                lightDirectionSlot2 = tmp
            )
        }
    }

    private fun closeDropdownMenu() {
        updateSuccessState { it.copy(isDropdownMenuExpanded = false) }
    }

    private fun showDropdownMenu() {
        updateSuccessState { it.copy(isDropdownMenuExpanded = true) }
    }

    private fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val imageSlot1 = imageStorageService.loadImage("slot1")
            val imageSlot2 = imageStorageService.loadImage("slot2")

            withContext(Dispatchers.Main) {
                updateSuccessState { it.copy(
                    capturedImageSlot1 = imageSlot1,
                    capturedImageSlot2 = imageSlot2
                ) }
            }
        }
    }

    private fun analyseImages() {
        _state.value.takeIf { it is PhotosSummaryScreenState.Success }?.let {
            val successState = it as PhotosSummaryScreenState.Success

            storeCurrentState() // if error comes the correct state is stored

            _state.value = successState.copy(
                isLoadingDialogShown = true
            )
            // from now on the success state does not have same values like the _state because _state.value was updated
            // but successState is still the same from the beginning of the function

            viewModelScope.launch {
                try {
                    val storeInDb = settingsRepository.getSendDataToServerChoice()

                    val material = remoteMaterialRepository.analyseMaterial(
                        firstImageLightDirection = successState.lightDirectionSlot1,
                        name = successState.materialName,
                        category = successState.selectedCategory,
                        storeInDb = storeInDb
                    )

                    val materialId = localMaterialRepository.insertMaterial(material)

                    // todo ted ukladam specular (coz je zatim ten, na ktery se sviti zleva), pak se lze dohodnout, jaky budeme ukladat
                    val imageToStore = if (successState.lightDirectionSlot1 == LightDirection.FROM_LEFT)
                        successState.capturedImageSlot1 else successState.capturedImageSlot2
                    imageStorageService.storeImage(imageToStore!!, materialId.toString())

                    imageStorageService.deleteImage("slot1")
                    imageStorageService.deleteImage("slot2")

                    _state.update { successState.copy(
                        capturedImageSlot1 = null,
                        capturedImageSlot2 = null,
                        isLoadingDialogShown = false
                    ) }

                    _navigationEvents.emit(PhotosSummaryNavigationEvent.ToPolarPlotVisualisationScreen(
                        firstMaterialId = materialId,
                        firstMaterialName = successState.materialName
                    ))
                }
                catch (e: NoInternetException) {
                    _state.value = PhotosSummaryScreenState.Error(
                        messageResId = R.string.no_internet_exception,
                        exception = e
                    )
                }
                catch (e: IOException) {
                    _state.value = PhotosSummaryScreenState.Error(
                        messageResId = R.string.io_exception,
                        exception = e
                    )
                }
                catch (e: Exception) {
                    _state.value = PhotosSummaryScreenState.Error(
                        messageResId = R.string.unknown_exception,
                        exception = e
                    )
                }
            }
        }
    }

    private fun goBackFromErrorScreen() {
        resetStateAfterError()
    }
}