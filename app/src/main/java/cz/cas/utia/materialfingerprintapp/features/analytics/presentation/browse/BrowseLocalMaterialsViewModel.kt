package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsProtoDataStore
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
            val isSecondMaterialChecked = _checkedMaterials.value.size > 1

            _navigationEvents.emit(MaterialNavigationEvent.ToPolarPlotVisualisationScreen(
                isFirstMaterialSourceLocal = true,
                firstMaterialId = _checkedMaterials.value.first(),
                isSecondMaterialSourceLocal = if (isSecondMaterialChecked) true else null,
                secondMaterialId = if (isSecondMaterialChecked) _checkedMaterials.value.elementAt(1) else null,
            ))
        }
    }
}