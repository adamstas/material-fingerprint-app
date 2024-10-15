package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialDao
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

//todo pridat nejaky Repository jeste mezi k ViewvModelu a Dao?

class BrowseMaterialsViewModel(
    private val dao: MaterialDao //todo later add dependency injection
): ViewModel() {

    //private val _isDropDownMenuExpanded = MutableStateFlow //todo rozmyslet, ktere promenne budou jen ve statu a ktere i tady s podtrzitkem

    private val _selectedCategoryIDs = MutableStateFlow((0..<MaterialCategory.entries.size).toList())



    @OptIn(ExperimentalCoroutinesApi::class)
    private val _materials = _selectedCategoryIDs.flatMapLatest { selectedCategories ->
        dao.getMaterialsOrderedByName(MaterialCategory.fromIDs(selectedCategories))
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _materialUIElements = _materials.map { materials ->
        materials.map { material ->
            MaterialUIElement(
                material = material,
                checked = false
            )
        }
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _searchText = MutableStateFlow("") //for search bar
    //val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false) //for search bar
   // val isSearching = _isSearching.asStateFlow()

    private val _state = MutableStateFlow(MaterialsScreenState())
    val state = combine(_state, _selectedCategoryIDs, _materials, _materialUIElements)
    { state, selectedCategoryIDs, materials, materialUIElements ->
        state.copy(
            selectedCategoryIDs = selectedCategoryIDs,
            materials = materials,
            materialUIElements = materialUIElements
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaterialsScreenState())

    fun onEvent(event: MaterialEvent) {
        when (event) {
            is MaterialEvent.CheckOrUncheckMaterial -> {
                _state.update { currentState ->
                    val changedMaterialIndex = currentState.materialUIElements.indexOfFirst {
                        it.material.id == event.materialUIElement.material.id
                    }
                    //there was check if material was found but i deleted it - was it needed? (if index is -1, return current state)
                    val updatedMaterialUIElements = currentState.materialUIElements.toMutableList().apply { //todo udelat to lepe bez mutable listu? ale asi to neresit..
                        val material = this[changedMaterialIndex]
                        this[changedMaterialIndex] = material.copy(checked = !material.checked)
                        //todo add list of checked materials so they are easy to get when user wants to create polar plot
                    }
                    currentState.copy(materialUIElements = updatedMaterialUIElements)
                    //todo can keep list of materials and also keep idx of every material in the materials itself => finding checked/unchecked material would take O(1) but code less simple -> test if slow right now
                }
            }
            is MaterialEvent.FilterMaterialsByCategory -> {
                _selectedCategoryIDs.value = event.categoryIDs

//                val updatedMaterialsFlow = dao.getMaterialsOrderedByName(event.categories)
//                val updatedMaterialsUIElements = updatedMaterialsFlow.mapLatest { updatedMaterials ->
//                    updatedMaterials.map { updatedMaterial ->
//                        MaterialUIElement(
//                            material = updatedMaterial,
//                            checked = false
//                        )
//                    }
//                }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()) })
//
//                 _materials.value = updatedMaterialsUIElements
//
//                _state.update { currentState ->
//                    currentState.copy(materials = updatedMaterialsUIElements)
//                }

            }
            is MaterialEvent.SetName -> TODO()
            is MaterialEvent.SetServerId -> TODO()

            is MaterialEvent.CheckOrUncheckCategory -> {
               // _selectedCategoryIDs.value = event.categoryID

                var updatedList = _selectedCategoryIDs.value
                if (event.categoryID in _selectedCategoryIDs.value) {
                    //unchecked the category
                    updatedList = updatedList - event.categoryID
                } else {
                    //checked the category
                    updatedList = updatedList + event.categoryID
                }
                _selectedCategoryIDs.value = updatedList.sortedBy { id ->
                    MaterialCategory.entries[id].ordinal //todo je to ok? vyzkouset az bude funkcni DB
                }
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
        }
    }
}