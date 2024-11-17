package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//todo umistit jinam nez do presentation? ..treba rozdelit packagem na local a remote, jinak to nechat v prezentacni vrstve
@OptIn(FlowPreview::class)
class BrowseRemoteMaterialsViewModel(
    materialRepository: MaterialRepository
): BrowseMaterialsViewModel(materialRepository) {

    init {
        _searchBarText
            .debounce(700L)
            .onEach {
                    _state.update { it.copy(isSearching = true) }
                //todo add isSearching changes for LocalViewModel too? probably not, for local materials it should load instantly..
                    filterMaterials()
                    _state.update { it.copy(isSearching = false) }
                }.launchIn(viewModelScope)

    }

    override fun closeDropdownMenu() {
        _state.update { it.copy(isDropdownMenuExpanded = false) }
        //get fresh materials after closing the dropdown menu
        viewModelScope.launch { //todo maybe try catch if the API service will be offline or no internet?
            _state.update { it.copy(isSearching = true) }
            filterMaterials()
            _state.update { it.copy(isSearching = false) }
        }
    }


}