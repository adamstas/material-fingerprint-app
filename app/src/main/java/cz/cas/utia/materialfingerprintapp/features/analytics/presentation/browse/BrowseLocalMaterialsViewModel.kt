package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class BrowseLocalMaterialsViewModel(
    materialRepository: MaterialRepository
): BrowseMaterialsViewModel(materialRepository) {

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
}