package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialDao
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

//todo pridat nejaky Repository jeste mezi k ViewvModelu a Dao?

class BrowseMaterialsViewModel(
    private val dao: MaterialDao //todo later add dependency injection
): ViewModel() {
    //ty private atributy musim updatovat primo a ne z _state protoze ten _state je ma neaktualni a ty spravne se tam davaji az v voleani combine, kde vznika state pro UI
    private val _selectedCategoryIDs = MutableStateFlow((0..<MaterialCategory.entries.size).toList())
    private val _checkedMaterials = MutableStateFlow<Set<Int>>(emptySet()) //mutable set wont notify compose so it wont render the UI after change in the mutable set
    private val _materials = MutableStateFlow<List<Material>>(emptyList()) //todo defaultne by mely byt vsechny materialy..
    private val _searchBarText = MutableStateFlow("")
    private val _isSearching = MutableStateFlow(false) //for search bar
    //todo use when implementing viewmodel for OnlineBrowseMaterialsScreen where the circular loading animation will be

    private val _state = MutableStateFlow(MaterialsScreenState())
    val state = combine(_state, _selectedCategoryIDs, _materials, _checkedMaterials, _searchBarText)
    { state, selectedCategoryIDs, materials, checkedMaterials, searchBarText ->
        state.copy(
            selectedCategoryIDs = selectedCategoryIDs,
            materials = materials,
            checkedMaterials = checkedMaterials,
            searchBarText = searchBarText
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaterialsScreenState())

    private fun enableOrDisableFindSimilarMaterialButton() {
        val enable = _checkedMaterials.value.size == 1
        _state.update {
            it.copy(
                isFindSimilarMaterialButtonEnabled = enable
            )
        }
    }

    private fun enableOrDisableCreatePolarPlotButton() {
        val enable = _checkedMaterials.value.size in 1..2
        _state.update {
            it.copy(
                isCreatePolarPlotButtonEnabled = enable
            )
        }
    }

    private fun updateButtons() {
        enableOrDisableFindSimilarMaterialButton()
        enableOrDisableCreatePolarPlotButton()
    }

    private fun updateSelectedCategoriesText() {
        _state.update {
            it.copy(
                selectedCategoriesText = when (_selectedCategoryIDs.value.size) {
                    0 -> "None selected"
                    1 -> MaterialCategory.fromIDs(_selectedCategoryIDs.value).first().toString()
                    else -> "Selected ${_selectedCategoryIDs.value.size}"
                }
            )
        }
    }

    private suspend fun filterMaterials() {
        _materials.value = dao.getMaterialsOrderedByName(MaterialCategory.fromIDs(_selectedCategoryIDs.value), _searchBarText.value)
    }

    init {
        viewModelScope.launch { //todo check if this works after setting up DI
            _materials.value = dao.getAllMaterialsOrderedByName()
            Log.i("ONEVENT", "materials: " + dao.getAllMaterialsOrderedByName()) //this should NOT return empty list
        }

        _materials.onEach { _ ->
            _checkedMaterials.value = emptySet()
            updateButtons()
        }.launchIn(viewModelScope)

        _checkedMaterials.onEach {
            updateButtons()
        }.launchIn(viewModelScope)

        _searchBarText.onEach {
            filterMaterials()
        }.launchIn(viewModelScope)

        _selectedCategoryIDs.onEach {
            filterMaterials()
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MaterialEvent) {
        when (event) {
            is MaterialEvent.CheckMaterial -> {
                _checkedMaterials.value += event.materialID
            }

            is MaterialEvent.UncheckMaterial -> {
                _checkedMaterials.value -= event.materialID
            }

            is MaterialEvent.FilterMaterialsByCategory -> { //todo unused? mozna pouzit kdyz vyjede z Categories dropdownMenu
                _selectedCategoryIDs.value = event.categoryIDs
            }

            is MaterialEvent.SetName -> TODO()
            is MaterialEvent.SetServerId -> TODO()

            is MaterialEvent.CheckOrUncheckCategory -> {
                val updatedList = if (event.categoryID in _selectedCategoryIDs.value) {
                    //unchecked the category
                    _selectedCategoryIDs.value - event.categoryID
                } else {
                    //checked the category
                    _selectedCategoryIDs.value + event.categoryID
                }
                _selectedCategoryIDs.value = updatedList.sortedBy { id ->
                    MaterialCategory.entries[id].ordinal
                }
                updateSelectedCategoriesText()
            }

           is MaterialEvent.ShowOrHideDropdownMenu -> {
                _state.update { it.copy(
                    isDropdownMenuExpanded = event.newState
                ) }
            }

            MaterialEvent.CloseDropdownMenu -> {
                _state.update { it.copy(
                    isDropdownMenuExpanded = false
                ) }
            }

            is MaterialEvent.SearchMaterials -> {
                _searchBarText.value = event.searchedText
            }
        }
    }
}