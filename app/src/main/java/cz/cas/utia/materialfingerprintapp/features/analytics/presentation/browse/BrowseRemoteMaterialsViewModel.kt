package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsStorageSlot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//todo umistit jinam nez do presentation? ..treba rozdelit packagem na local a remote, jinak to nechat v prezentacni vrstve
@OptIn(FlowPreview::class)
@HiltViewModel //todo enable these hilt commands after creating working remote repository
class BrowseRemoteMaterialsViewModel @Inject constructor(
    //materialRepository: RemoteMaterialRepository,
    materialRepository: LocalMaterialRepository, // todo remove this and use the remote repository later
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

            _navigationEvents.emit(MaterialNavigationEvent.ToPolarPlotVisualisationScreen(
                isFirstMaterialSourceLocal = false,
                firstMaterialId = firstCheckedMaterial.id,
                firstMaterialName = firstCheckedMaterial.name,
                isSecondMaterialSourceLocal = if (isSecondMaterialChecked) false else null,
                secondMaterialId = secondCheckedMaterial?.id,
                secondMaterialName = secondCheckedMaterial?.name
            ))
        }
    }

    override fun findSimilarLocalMaterials(event: MaterialEvent.FindSimilarLocalMaterials) {
        viewModelScope.launch {
            materialCharacteristicsRepository.saveMaterialCharacteristics(
                materialCharacteristics = event.material.characteristics,
                slot = MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN
            )

            _navigationEvents.emit(MaterialNavigationEvent.ToBrowseSimilarLocalMaterialsScreen(-1L))
        }
    }

    override fun findSimilarRemoteMaterials(event: MaterialEvent.FindSimilarRemoteMaterials) {
        viewModelScope.launch {
            _navigationEvents.emit(MaterialNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen(event.material.id))
        }
    }
}