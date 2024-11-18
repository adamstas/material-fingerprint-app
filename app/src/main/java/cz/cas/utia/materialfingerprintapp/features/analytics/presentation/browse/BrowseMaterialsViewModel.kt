package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BrowseMaterialsViewModel(
    private val materialRepository: MaterialRepository
): ViewModel() {
    //ty private atributy musim updatovat primo a ne z _state protoze ten _state je ma neaktualni a ty spravne se tam davaji az v voleani combine, kde vznika state pro UI
    protected val _selectedCategoryIDs = MutableStateFlow((0..<MaterialCategory.entries.size).toList())
    private val _checkedMaterials = MutableStateFlow<Set<Int>>(emptySet()) //mutable set wont notify compose so it wont render the UI after change in the mutable set
    private val _materials = MutableStateFlow<List<Material>>(emptyList()) //todo defaultne by mely byt vsechny materialy..
    protected val _searchBarText = MutableStateFlow("")

    protected val _state = MutableStateFlow(MaterialsScreenState())
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
                    MaterialCategory.entries.size -> "All selected"
                    else -> "Selected ${_selectedCategoryIDs.value.size}"
                }
            )
        }
    }

    protected suspend fun filterMaterials() {
        _materials.value = materialRepository.getMaterialsOrderedByName(MaterialCategory.fromIDs(_selectedCategoryIDs.value), _searchBarText.value)
    }

    init {
        viewModelScope.launch { //todo check if this works after setting up DI
            _materials.value = materialRepository.getAllMaterialsOrderedByName()
            Log.i("ONEVENT", "materials: " + materialRepository.getAllMaterialsOrderedByName()) //this should NOT return empty list
        }

        _materials.onEach { _ ->
            _checkedMaterials.value = emptySet()
            updateButtons()
        }.launchIn(viewModelScope)

        _checkedMaterials.onEach {
            updateButtons()
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: MaterialEvent) {
        when (event) {
            is MaterialEvent.CheckMaterial -> checkMaterial(event)
            is MaterialEvent.UncheckMaterial -> uncheckMaterial(event)

            is MaterialEvent.SetName -> TODO()
            is MaterialEvent.SetServerId -> TODO()

            is MaterialEvent.CheckOrUncheckCategory -> checkOrUncheckCategory(event)
            MaterialEvent.ShowDropdownMenu -> showDropdownMenu()
            MaterialEvent.CloseDropdownMenu -> closeDropdownMenu()
            is MaterialEvent.SearchMaterials -> searchMaterials(event)
        }
    }

    private fun checkMaterial(event: MaterialEvent.CheckMaterial) {
        _checkedMaterials.value += event.materialID
    }

    private fun uncheckMaterial(event: MaterialEvent.UncheckMaterial) {
        _checkedMaterials.value -= event.materialID
    }

    private fun checkOrUncheckCategory(event: MaterialEvent.CheckOrUncheckCategory) {
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

    private fun showDropdownMenu() {
        _state.update { it.copy(
            isDropdownMenuExpanded = true
        ) }
    }

    private fun searchMaterials(event: MaterialEvent.SearchMaterials) {
        _searchBarText.value = event.searchedText
    }

    protected abstract fun closeDropdownMenu()
}