package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analysis.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.data.repository.MaterialCharacteristicsProtoDataStore
import cz.cas.utia.materialfingerprintapp.features.analysis.data.repository.MaterialCharacteristicsStorageSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    // state flow has a default value so when this _selectedCategoryIDs.onEach { ... } is set in init block, it has to immediately call filterMaterials()
    // because there is a value to be emitted already
    init {
        combine( // combine those flows so filterMaterials() is not called twice when ViewModel is created
            // (otherwise it would call filterMaterials() twice because there would be initial change in both flows)
            _selectedCategoryIDs,
            _searchBarText
        ) { selectedCategoryIDs, searchBarText ->
            // combined flow emits a Pair of current values
            Pair(selectedCategoryIDs, searchBarText)
        }
            .distinctUntilChanged()
            .onEach {
                filterMaterials()
            }
            .launchIn(viewModelScope)
    }

    override fun closeDropdownMenu() {
        updateSuccessState { it.copy(isDropdownMenuExpanded = false) }
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