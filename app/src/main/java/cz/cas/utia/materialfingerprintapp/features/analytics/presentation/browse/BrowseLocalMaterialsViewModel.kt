package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsProtoDataStore
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsStorageSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseLocalMaterialsViewModel @Inject constructor(
    materialRepository: LocalMaterialRepository,
    savedStateHandle: SavedStateHandle,
    dataStore: MaterialCharacteristicsProtoDataStore
): BrowseMaterialsViewModel(
    materialRepository = materialRepository,
    savedStateHandle = savedStateHandle,
    materialCharacteristicsRepository = dataStore) {

    init {
        _selectedCategoryIDs.onEach {
            filterMaterials()
        }.launchIn(viewModelScope)

        _searchBarText.onEach {
            filterMaterials()
        }.launchIn(viewModelScope)
    }

    override fun closeDropdownMenu() {
        _state.update { it.copy(
            isDropdownMenuExpanded = false
        ) }
    }

    override fun createPolarPlot() {
        viewModelScope.launch {
            val firstCheckedMaterial = _checkedMaterials.value.first()
            val isSecondMaterialChecked = _checkedMaterials.value.size > 1
            val secondCheckedMaterial = if (isSecondMaterialChecked) _checkedMaterials.value.elementAt(1) else null

            _navigationEvents.emit(MaterialNavigationEvent.ToPolarPlotVisualisationScreen(
                isFirstMaterialSourceLocal = true,
                firstMaterialId = firstCheckedMaterial.id,
                firstMaterialName = firstCheckedMaterial.name,
                isSecondMaterialSourceLocal = if (isSecondMaterialChecked) true else null,
                secondMaterialId = secondCheckedMaterial?.id,
                secondMaterialName = secondCheckedMaterial?.name
            ))
        }
    }

    override fun findSimilarLocalMaterials(event: MaterialEvent.FindSimilarLocalMaterials) {
        viewModelScope.launch {
            _navigationEvents.emit(MaterialNavigationEvent.ToBrowseSimilarLocalMaterialsScreen(event.material.id))
        }
    }

    override fun findSimilarRemoteMaterials(event: MaterialEvent.FindSimilarRemoteMaterials) {
        viewModelScope.launch {
            materialCharacteristicsRepository.saveMaterialCharacteristics(
                materialCharacteristics = event.material.characteristics,
                slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
            )

            _navigationEvents.emit(MaterialNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen(-1L))
        }
    }
}