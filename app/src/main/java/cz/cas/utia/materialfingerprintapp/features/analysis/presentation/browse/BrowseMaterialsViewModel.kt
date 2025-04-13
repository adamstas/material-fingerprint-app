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
    //private atributy jsou to proto, ze je pri jejich zmene potreba udelat nejakou reakci (napr. pri zmene _materials je potreba updatovat tlacitka
    //napr. _checkedMaterials by se mohlo brat z public statu, ale pak by neslo reagovat na zmenu toho _checkedMaterials
    //_searchBarText tu taky musi byt jako private, protoze je na nej v reakce v BrowseLocalMaterialsViewModelu, totez pro _selectedCategoryIDs
    //ty private atributy musim updatovat primo a ne z _state protoze ten _state je ma neaktualni a ty spravne se tam davaji az v volani combine, kde vznika state pro UI
    protected val _selectedCategoryIDs = MutableStateFlow((0..<MaterialCategory.entries.size).toList())
    protected val _checkedMaterials = MutableStateFlow<Set<MaterialSummary>>(emptySet()) //mutable set wont notify compose so it wont render the UI after change in the mutable set
    protected val _materials = MutableStateFlow<List<MaterialSummary>>(emptyList()) //todo defaultne by mely byt vsechny materialy..
    protected val _searchBarText = MutableStateFlow("")

    private val _similarMaterialId = savedStateHandle.get<Long?>("materialId") // cannot use toRoute since the ViewModel can be in 2 routes (BrowseSimilarMaterials and BrowseMaterials)

    protected val _state = MutableStateFlow<MaterialsScreenState>(MaterialsScreenState.Success())
    val state = combine(_state, _selectedCategoryIDs, _materials, _checkedMaterials, _searchBarText)
    { state, selectedCategoryIDs, materials, checkedMaterials, searchBarText ->

        when (state) {
            is MaterialsScreenState.Success -> {
                state.copy(
                    selectedCategoryIDs = selectedCategoryIDs,
                    materials = materials,
                    checkedMaterials = checkedMaterials,
                    searchBarText = searchBarText
                )
            }

            is MaterialsScreenState.Error -> state
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MaterialsScreenState.Success())

    protected val _navigationEvents = MutableSharedFlow<MaterialNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    // helper for less boiler plate code
    protected fun updateSuccessState(update: (MaterialsScreenState.Success) -> MaterialsScreenState) {
        val currentState = _state.value
        if (currentState is MaterialsScreenState.Success) {
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
            _state.value = MaterialsScreenState.Error(
                messageResId = R.string.no_internet_exception,
                exception = e
            )
        }
        catch (e: IOException) {
            _state.value = MaterialsScreenState.Error(
                messageResId = R.string.io_exception,
                exception = e
            )
        }
        catch (e: Exception) {
            _state.value = MaterialsScreenState.Error(
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
            is MaterialEvent.FindSimilarLocalMaterials -> findSimilarLocalMaterials(event)
            is MaterialEvent.FindSimilarRemoteMaterials -> findSimilarRemoteMaterials(event)
            MaterialEvent.CreatePolarPlot -> createPolarPlot()
            MaterialEvent.DismissFindSimilarMaterialsDialog -> dismissFindSimilarMaterialsDialog()
            MaterialEvent.FindSimilarMaterial -> findSimilarMaterials()
            MaterialEvent.GoBack -> goBack()
        }
    }

    private fun checkMaterial(event: MaterialEvent.CheckMaterial) {
        _checkedMaterials.value += event.material
    }

    private fun uncheckMaterial(event: MaterialEvent.UncheckMaterial) {
        _checkedMaterials.value -= event.material
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
        updateSuccessState { it.copy(isDropdownMenuExpanded = true) }
    }

    private fun searchMaterials(event: MaterialEvent.SearchMaterials) {
        _searchBarText.value = event.searchedText
    }

    protected abstract fun closeDropdownMenu()

    protected abstract fun findSimilarLocalMaterials(event: MaterialEvent.FindSimilarLocalMaterials)

    protected abstract fun findSimilarRemoteMaterials(event: MaterialEvent.FindSimilarRemoteMaterials)

    protected abstract fun createPolarPlot()

    private fun dismissFindSimilarMaterialsDialog() {
        updateSuccessState { it.copy(isFindSimilarMaterialsDialogShown = false) }
    }

    private fun findSimilarMaterials() {
        updateSuccessState { it.copy(isFindSimilarMaterialsDialogShown = true) }
    }

    private fun goBack() {
        viewModelScope.launch {
            _navigationEvents.emit(MaterialNavigationEvent.Back)
        }
    }
}