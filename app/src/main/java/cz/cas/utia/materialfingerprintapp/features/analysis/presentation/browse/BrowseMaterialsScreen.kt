package cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.components.DropDownMenuWithCheckboxesItem
import cz.cas.utia.materialfingerprintapp.core.ui.components.DropdownMenuWithCheckboxes
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import coil3.compose.AsyncImage
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.core.ui.components.NavigationHandler
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.commoncomponents.FindSimilarMaterialsDialog
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.commoncomponents.PolarPlotCanvas
import cz.cas.utia.materialfingerprintapp.core.AppConfig.Server.GET_MATERIAL_IMAGE_URL_APPEND
import cz.cas.utia.materialfingerprintapp.core.AppConfig.Server.MATERIALS_URL
import cz.cas.utia.materialfingerprintapp.core.ui.components.ErrorScreen
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialImage

@Composable
fun BrowseMaterialsScreenRoot(
    navigateBack: () -> Unit,
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToPolarPlotVisualisationScreen: (Boolean, Long, String, Boolean?, Long?, String?) -> Unit,
    viewModel: BrowseMaterialsViewModel,
    title: String
) {
    val state by viewModel.state.collectAsState()

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                is BrowseMaterialsNavigationEvent.ToBrowseSimilarLocalMaterialsScreen -> navigateToBrowseSimilarLocalMaterialsScreen(event.materialID)
                is BrowseMaterialsNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen -> navigateToBrowseSimilarRemoteMaterialsScreen(event.materialID)
                is BrowseMaterialsNavigationEvent.ToPolarPlotVisualisationScreen -> navigateToPolarPlotVisualisationScreen(
                    event.isFirstMaterialSourceLocal,
                    event.firstMaterialId,
                    event.firstMaterialName,
                    event.isSecondMaterialSourceLocal,
                    event.secondMaterialId,
                    event.secondMaterialName
                )
                BrowseMaterialsNavigationEvent.Back -> navigateBack()
            }
        }
    )

    when (val screenState = state) {
        is BrowseMaterialsScreenState.Success -> BrowseMaterialsScreen(
            title = title,
            state = screenState,
            onEvent = viewModel::onEvent
        )

        is BrowseMaterialsScreenState.Error -> ErrorScreen(
            message = stringResource(id = screenState.messageResId),
            onAction = { viewModel.onEvent(BrowseMaterialsEvent.GoBack) },
            buttonText = "Go back",
            exception = screenState.exception
        )
    }
}

@Composable
fun BrowseMaterialsScreen(
    title: String,
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBarTitle(
                title = title,
                navigateBack = { onEvent(BrowseMaterialsEvent.GoBack) }
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .windowInsetsPadding(WindowInsets.navigationBars)
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
                onEvent = onEvent,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (state.isFindSimilarMaterialsDialogShown) {
                FindSimilarMaterialsDialog(
                    onDismissRequest = { onEvent(BrowseMaterialsEvent.DismissFindSimilarMaterialsDialog) },
                    onLocalDatabaseButtonClick = { onEvent(BrowseMaterialsEvent.FindSimilarLocalMaterials(state.getFirstCheckedMaterial())) },
                    onRemoteDatabaseButtonClick = { onEvent(BrowseMaterialsEvent.FindSimilarRemoteMaterials(state.getFirstCheckedMaterial())) }
                )
            }
        }
    }
}

@Composable
fun SearchAndFilterSection(
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MaterialsSearchBar(
            state,
            onEvent,
            Modifier.weight(0.7f)
        )
        Spacer(modifier = Modifier.size(8.dp))

        CategoriesDropdownMenu(
            state,
            onEvent,
            Modifier.weight(0.7f)
        )
    }
}

@Composable
fun MaterialsSearchBar(
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = state.searchBarText,
        onValueChange = { searchText: String ->
            onEvent(BrowseMaterialsEvent.SearchMaterials(searchText))
            },
        placeholder = { Text(text = "Search for name...") },
        singleLine = true,
        modifier = modifier
    )
    //maybe put the search bar up the categories top down menu in case the top down menu is too wide
}

@Composable
fun CategoriesDropdownMenu(
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit,
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
                onEvent(BrowseMaterialsEvent.CheckOrUncheckCategory(id))
            },
            expanded = state.isDropdownMenuExpanded,
            onDropdownMenuClick = { onEvent(BrowseMaterialsEvent.ShowDropdownMenu) },
            onDropdownMenuClosed = { onEvent(BrowseMaterialsEvent.CloseDropdownMenu) }
        )
    }
}

@Composable
fun MaterialsListSection(
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit,
    modifier: Modifier
) {
    if (state.isSearching) {
        CenterBox(
            modifier = modifier,
            content = { CircularProgressIndicator() }
        )
    }
    else if (state.isMaterialsListEmpty()) {
        CenterBox(
            modifier = modifier,
            content = {
                Text(
                    text = "No material found",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        )
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
    material: MaterialSummary,
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit
) {
    val imageSize = 96.dp

    Text(text = material.name)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (material.photoThumbnail) {
            is MaterialImage.BitmapImage ->
                Image(
                    bitmap = material.photoThumbnail.imageBitmap,
                    contentDescription = "Material specular image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(imageSize)
                )

            MaterialImage.UrlImage ->
                AsyncImage(
                    model = MATERIALS_URL + material.id + GET_MATERIAL_IMAGE_URL_APPEND,
                    contentDescription = "Material specular image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(imageSize)
                )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Box(
            modifier = Modifier.size(imageSize)
        ) {
            PolarPlotCanvas(
                axisValues = material.characteristics.toListForDrawing(),
                firstPlotColor = colorResource(id = AppConfig.Colors.primaryPlotColorId),
                pointRadius = 4f,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = material.category.toString(),
            style = MaterialTheme.typography.bodySmall
        )

        Checkbox(
            checked = state.isMaterialChecked(material),
            onCheckedChange = { checked ->
                if (checked) {
                    onEvent(BrowseMaterialsEvent.CheckMaterial(material))
                } else {
                    onEvent(BrowseMaterialsEvent.UncheckMaterial(material))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End))
    }
}

@Composable
fun BottomButtonsSection(
    state: BrowseMaterialsScreenState.Success,
    onEvent: (BrowseMaterialsEvent) -> Unit,
    modifier: Modifier) {
        Button(
            modifier = modifier,
            enabled = state.isFindSimilarMaterialButtonEnabled,
            onClick = { onEvent(BrowseMaterialsEvent.FindSimilarMaterial) }) {
                Text(text = "Find similar materials")
        }

        Button(
            modifier = modifier,
            enabled = state.isCreatePolarPlotButtonEnabled,
            onClick = { onEvent(BrowseMaterialsEvent.CreatePolarPlot) }) {
                Text(text = "Create polar plot")
        }
}

@Composable
fun CenterBox(
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Box(
        //use the modifier in parameters (otherwise the buttons go away) and apply fillMaxSize() to it
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}