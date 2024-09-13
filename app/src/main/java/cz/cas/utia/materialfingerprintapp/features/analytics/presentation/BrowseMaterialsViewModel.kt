package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import androidx.lifecycle.ViewModel
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialDao
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BrowseMaterialsViewModel(
    private val dao: MaterialDao //todo later add dependency injection
): ViewModel() {

    private val _materials = MutableStateFlow(listOf<MaterialUIElement>()) //mozna neni potreba, zjistit jeste

    private val _searchText = MutableStateFlow("") //for search bar
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false) //for search bar
    val isSearching = _isSearching.asStateFlow()

    private val _state = MutableStateFlow(MaterialsScreenState())

    fun onEvent(event: MaterialEvent) {
        when (event) {
            is MaterialEvent.CheckOrUncheckMaterial -> {
                _state.update { currentState ->
                    val changedMaterialIndex = currentState.materials.indexOfFirst {
                        it.material.id == event.materialUIElement.material.id
                    }
                    //there was check if material was found but i deleted it - was it needed? (if index is -1, return current state)
                    val updatedMaterials = currentState.materials.toMutableList().apply { //todo udelat to lepe bez mutable listu? ale asi to neresit..
                        val material = this[changedMaterialIndex]
                        this[changedMaterialIndex] = material.copy(checked = !material.checked)
                        //todo add list of checked materials so they are easy to get when user wants to create polar plot
                    }
                    currentState.copy(materials = updatedMaterials)
                    //todo can keep list of materials and also keep idx of every material in the materials itself => finding checked/unchecked material would take O(1) but code less simple -> test if slow right now
                }
            }
            is MaterialEvent.FilterMaterialsByCategory -> {
                //todo prekreslit UI a vytahnout z dao nova data (nejak naparsovat SQL dotaz a pouzit list kategorii, ktere mam u tohoto eventu)

            }
            is MaterialEvent.SetName -> TODO()
            is MaterialEvent.SetServerId -> TODO()
        }
    }
}