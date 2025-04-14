package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsStorageSlot
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.RemoteMaterialRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class BrowseRemoteMaterialsViewModel @Inject constructor(
    materialRepository: RemoteMaterialRepository,
    savedStateHandle: SavedStateHandle,
    materialCharacteristicsRepository: MaterialCharacteristicsRepository
): BrowseMaterialsViewModel(
    materialRepository = materialRepository,
    savedStateHandle = savedStateHandle,
    materialCharacteristicsRepository = materialCharacteristicsRepository) {

    init {
        _searchBarText
            .debounce(700L)
            .onEach {
                    updateSuccessState { it.copy(isSearching = true) }
                    filterMaterials()
                    updateSuccessState { it.copy(isSearching = false) }
                }.launchIn(viewModelScope)

    }

    override fun closeDropdownMenu() {
        updateSuccessState { it.copy(isDropdownMenuExpanded = false) }
        //get fresh materials after closing the dropdown menu
        viewModelScope.launch {
            updateSuccessState { it.copy(isSearching = true) }
            filterMaterials()
            updateSuccessState { it.copy(isSearching = false) }
        }
    }

    override fun createPolarPlot() {
        viewModelScope.launch {
            val firstCheckedMaterial = _checkedMaterials.value.first()
            val isSecondMaterialChecked = _checkedMaterials.value.size > 1
            val secondCheckedMaterial = if (isSecondMaterialChecked) _checkedMaterials.value.elementAt(1) else null

            materialCharacteristicsRepository.saveMaterialCharacteristics(
                materialCharacteristics = firstCheckedMaterial.characteristics,
                slot = MaterialCharacteristicsStorageSlot.REMOTE_FIRST
            )

            secondCheckedMaterial?.let {
                materialCharacteristicsRepository.saveMaterialCharacteristics(
                    materialCharacteristics = secondCheckedMaterial.characteristics,
                    slot = MaterialCharacteristicsStorageSlot.REMOTE_SECOND
                )
            }

            _navigationEvents.emit(BrowseMaterialsNavigationEvent.ToPolarPlotVisualisationScreen(
                isFirstMaterialSourceLocal = false,
                firstMaterialId = firstCheckedMaterial.id,
                firstMaterialName = firstCheckedMaterial.name,
                isSecondMaterialSourceLocal = if (isSecondMaterialChecked) false else null,
                secondMaterialId = secondCheckedMaterial?.id,
                secondMaterialName = secondCheckedMaterial?.name
            ))
        }
    }

    override fun findSimilarLocalMaterials(event: BrowseMaterialsEvent.FindSimilarLocalMaterials) {
        viewModelScope.launch {
            materialCharacteristicsRepository.saveMaterialCharacteristics(
                materialCharacteristics = event.material.characteristics,
                slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
            )

            _navigationEvents.emit(BrowseMaterialsNavigationEvent.ToBrowseSimilarLocalMaterialsScreen(-1L))
        }
    }

    override fun findSimilarRemoteMaterials(event: BrowseMaterialsEvent.FindSimilarRemoteMaterials) {
        viewModelScope.launch {
            _navigationEvents.emit(BrowseMaterialsNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen(event.material.id))
        }
    }
}