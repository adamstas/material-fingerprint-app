package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.components.DropDownMenuWithCheckboxesItem
import cz.cas.utia.materialfingerprintapp.core.ui.components.DropdownMenuWithCheckboxes
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialEvent

@Composable
fun BrowseMaterialsScreen(
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBarTitle(title = "Browse materials") //todo rozlisit jestli jsou to lokal/online data?
        }
    ) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .padding(25.dp)
                    .padding(bottom = 16.dp)
                    .windowInsetsPadding(WindowInsets.navigationBars) //todo pozor ze kdyz se mobil otoci tak spodní tlacitko zajede pod navigacni listu (ocividne to tenhle typ paddingu neotoci spolecne s obrazovkou)
                    .fillMaxSize()
            ) {
                SearchAndFilterSection(state, onEvent)

                CustomSpacer()

                MaterialsListSection(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = state,
                    onEvent = onEvent)

                BottomButtonsSection(
                    state = state,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
    }
}

@Composable
fun SearchAndFilterSection(
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MaterialsSearchBar(
            state,
            onEvent,
            Modifier.weight(0.8f)
        )
        Spacer(modifier = Modifier.size(10.dp))

        CategoriesDropdownMenu(
            state,
            onEvent,
            Modifier.weight(0.7f)
        )
    }
}

@Composable
fun MaterialsSearchBar(
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = state.searchBarText,
        onValueChange = { searchText: String ->
            onEvent(MaterialEvent.SearchMaterials(searchText))
            },
        placeholder = { Text(text = "Search...") },
        modifier = modifier
    )
    //maybe put the search bar up the categories top down menu in case the top down menu is too wide
}

@Composable
fun CategoriesDropdownMenu(
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit,
    modifier: Modifier
) {
    Box(modifier = modifier) {
        DropdownMenuWithCheckboxes(
            label = "Categories",
            options = MaterialCategory.entries.mapIndexed { index, category ->
                DropDownMenuWithCheckboxesItem(category.toString(), index)
            },
            selectedIDs = state.selectedCategoryIDs,
            selectedText = state.selectedCategoriesText,
            checkOrUncheckItem = { id: Int ->
                onEvent(MaterialEvent.CheckOrUncheckCategory(id))
            },
            expanded = state.isDropdownMenuExpanded,
            onDropdownMenuClick = { newState: Boolean ->
                onEvent(MaterialEvent.ShowOrHideDropdownMenu(newState))
            },
            onDropdownMenuClosed = { onEvent(MaterialEvent.CloseDropdownMenu) }
        )
    }
}

@Composable
fun MaterialsListSection(
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit,
    modifier: Modifier
) {
    if (state.isMaterialsListEmpty()) {
        Box(
            //use the modifier in parameters (otherwise the buttons go away) and apply fillMaxSize() to it
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No material found",
                style = MaterialTheme.typography.titleLarge
            )
        }

    } else {
        LazyColumn(modifier = modifier)
        {
            items(state.materials) { material ->
                MaterialListRow(
                    material = material,
                    state = state,
                    onEvent = onEvent
                )
                CustomHorizontalDivider()
            }
        }
    }
}

@Composable
fun MaterialListRow(
    material: Material,
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit
) {
        Text(text = material.name) //todo styling

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = material.photoPath),
                contentDescription = "Latka Image", //todo zmenit
                contentScale = ContentScale.Crop, //todo nechat?
                modifier = Modifier.size(96.dp) // todo adjust size so the polar plot is somehow readable
            )

            Spacer(modifier = Modifier.width(24.dp))

            Image( //polar plots wont have axes names so this should be enough size..or make bigger?
                painter = painterResource(id = material.fingerprintPath),
                contentDescription = "Latka fingerprint image",
                contentScale = ContentScale.Crop, //todo nechat?
                modifier = Modifier.size(96.dp)  // todo adjust size so the polar plot is somehow readable
            )

            Checkbox(
                checked = state.isMaterialChecked(material.id),
                onCheckedChange = { checked ->
                    if (checked) {
                        onEvent(MaterialEvent.CheckMaterial(material.id))
                    } else {
                        onEvent(MaterialEvent.UncheckMaterial(material.id))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End))
        }
}

@Composable
fun BottomButtonsSection(
    state: MaterialsScreenState,
    modifier: Modifier) {
        Button(
            modifier = modifier,
            enabled = state.isFindSimilarMaterialButtonEnabled,
            onClick = { /*TODO*/ }) {
                Text(text = "Find similar material")
        }

        Button(
            modifier = modifier,
            enabled = state.isCreatePolarPlotButtonEnabled,
            onClick = { /*TODO*/ }) {
                Text(text = "Create polar plot")
        }
}