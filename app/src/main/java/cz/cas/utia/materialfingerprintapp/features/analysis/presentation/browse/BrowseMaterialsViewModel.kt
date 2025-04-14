package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api.exception.NoInternetException
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsStorageSlot
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException

abstract class BrowseMaterialsViewModel(
    private val savedStateHandle: SavedStateHandle, //for fetching navigation arguments
    private val materialRepository: MaterialRepository,
    protected val materialCharacteristicsRepository: MaterialCharacteristicsRepository
): ViewModel() {
    protected val _selectedCategoryIDs = MutableStateFlow((0..<MaterialCategory.entries.size).toList())
    protected val _checkedMaterials = MutableStateFlow<Set<MaterialSummary>>(emptySet()) //mutable set wont notify compose so it wont render the UI after change in the mutable set
    protected val _materials = MutableStateFlow<List<MaterialSummary>>(emptyList())
    protected val _searchBarText = MutableStateFlow("")

    private val _similarMaterialId = savedStateHandle.get<Long?>("materialId") // cannot use toRoute since the ViewModel can be in 2 routes (BrowseSimilarMaterials and BrowseMaterials)

    protected val _state = MutableStateFlow<BrowseMaterialsScreenState>(BrowseMaterialsScreenState.Success())
    val state = combine(_state, _selectedCategoryIDs, _materials, _checkedMaterials, _searchBarText)
    { state, selectedCategoryIDs, materials, checkedMaterials, searchBarText ->

        when (state) {
            is BrowseMaterialsScreenState.Success -> {
                state.copy(
                    selectedCategoryIDs = selectedCategoryIDs,
                    materials = materials,
                    checkedMaterials = checkedMaterials,
                    searchBarText = searchBarText
                )
            }

            is BrowseMaterialsScreenState.Error -> state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BrowseMaterialsScreenState.Success())

    protected val _navigationEvents = MutableSharedFlow<BrowseMaterialsNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    // helper for less boiler plate code
    protected fun updateSuccessState(update: (BrowseMaterialsScreenState.Success) -> BrowseMaterialsScreenState) {
        val currentState = _state.value
        if (currentState is BrowseMaterialsScreenState.Success) {
            _state.value = update(currentState)
        }
    }

    private fun enableOrDisableFindSimilarMaterialButton() {
        val enable = _checkedMaterials.value.size == 1
        updateSuccessState { it.copy(isFindSimilarMaterialButtonEnabled = enable) }
    }

    private fun enableOrDisableCreatePolarPlotButton() {
        val enable = _checkedMaterials.value.size in 1..2
        updateSuccessState { it.copy(isCreatePolarPlotButtonEnabled = enable) }
    }

    private fun updateButtons() {
        enableOrDisableFindSimilarMaterialButton()
        enableOrDisableCreatePolarPlotButton()
    }

    private fun updateSelectedCategoriesText() {
        updateSuccessState { it.copy(
            selectedCategoriesText = when (_selectedCategoryIDs.value.size) {
                0 -> "None selected"
                1 -> MaterialCategory.fromIDs(_selectedCategoryIDs.value).first().toString()
                MaterialCategory.entries.size -> "All selected"
                else -> "Selected ${_selectedCategoryIDs.value.size}"
            }
        ) }
    }

    protected suspend fun filterMaterials() {
        try {
            when (_similarMaterialId) {
                null -> _materials.value = materialRepository.getMaterialsOrderedByName(MaterialCategory.fromIDs(_selectedCategoryIDs.value), _searchBarText.value)
                -1L -> {
                    val characteristics = materialCharacteristicsRepository
                        .loadMaterialCharacteristics(MaterialCharacteristicsStorageSlot.APPLY_FILTER_SCREEN)
                    _materials.value = materialRepository.getSimilarMaterials(
                        materialCharacteristics = characteristics,
                        categories = MaterialCategory.fromIDs(_selectedCategoryIDs.value),
                        nameSearch = _searchBarText.value)
                }
                else -> _materials.value = materialRepository.getSimilarMaterials(MaterialCategory.fromIDs(_selectedCategoryIDs.value), _searchBarText.value, _similarMaterialId)
            }
        }

        catch (e: NoInternetException) {
            _state.value = BrowseMaterialsScreenState.Error(
                messageResId = R.string.no_internet_exception,
                exception = e
            )
        }
        catch (e: IOException) {
            _state.value = BrowseMaterialsScreenState.Error(
                messageResId = R.string.io_exception,
                exception = e
            )
        }
        catch (e: Exception) {
            _state.value = BrowseMaterialsScreenState.Error(
                messageResId = R.string.unknown_exception,
                exception = e
            )
        }
    }

    init {
        _materials.onEach { _ ->
            _checkedMaterials.value = emptySet()
            updateButtons()
        }.launchIn(viewModelScope)

        _checkedMaterials.onEach {
            updateButtons()
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: BrowseMaterialsEvent) {
        when (event) {
            is BrowseMaterialsEvent.CheckMaterial -> checkMaterial(event)
            is BrowseMaterialsEvent.UncheckMaterial -> uncheckMaterial(event)
            is BrowseMaterialsEvent.CheckOrUncheckCategory -> checkOrUncheckCategory(event)
            BrowseMaterialsEvent.ShowDropdownMenu -> showDropdownMenu()
            BrowseMaterialsEvent.CloseDropdownMenu -> closeDropdownMenu()
            is BrowseMaterialsEvent.SearchMaterials -> searchMaterials(event)
            is BrowseMaterialsEvent.FindSimilarLocalMaterials -> findSimilarLocalMaterials(event)
            is BrowseMaterialsEvent.FindSimilarRemoteMaterials -> findSimilarRemoteMaterials(event)
            BrowseMaterialsEvent.CreatePolarPlot -> createPolarPlot()
            BrowseMaterialsEvent.DismissFindSimilarMaterialsDialog -> dismissFindSimilarMaterialsDialog()
            BrowseMaterialsEvent.FindSimilarMaterial -> findSimilarMaterials()
            BrowseMaterialsEvent.GoBack -> goBack()
        }
    }

    private fun checkMaterial(event: BrowseMaterialsEvent.CheckMaterial) {
        _checkedMaterials.value += event.material
    }

    private fun uncheckMaterial(event: BrowseMaterialsEvent.UncheckMaterial) {
        _checkedMaterials.value -= event.material
    }

    private fun checkOrUncheckCategory(event: BrowseMaterialsEvent.CheckOrUncheckCategory) {
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
        updateSuccessState { it.copy(isDropdownMenuExpanded = true) }
    }

    private fun searchMaterials(event: BrowseMaterialsEvent.SearchMaterials) {
        _searchBarText.value = event.searchedText
    }

    protected abstract fun closeDropdownMenu()

    protected abstract fun findSimilarLocalMaterials(event: BrowseMaterialsEvent.FindSimilarLocalMaterials)

    protected abstract fun findSimilarRemoteMaterials(event: BrowseMaterialsEvent.FindSimilarRemoteMaterials)

    protected abstract fun createPolarPlot()

    private fun dismissFindSimilarMaterialsDialog() {
        updateSuccessState { it.copy(isFindSimilarMaterialsDialogShown = false) }
    }

    private fun findSimilarMaterials() {
        updateSuccessState { it.copy(isFindSimilarMaterialsDialogShown = true) }
    }

    private fun goBack() {
        viewModelScope.launch {
            _navigationEvents.emit(BrowseMaterialsNavigationEvent.Back)
        }
    }
}