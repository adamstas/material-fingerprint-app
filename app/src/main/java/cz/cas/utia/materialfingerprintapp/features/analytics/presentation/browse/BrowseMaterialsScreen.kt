package cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse

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
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.core.navigation.NavigationHandler
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents.FindSimilarMaterialsDialog
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.commoncomponents.PolarPlotCanvas
import kotlinx.coroutines.flow.SharedFlow
import cz.cas.utia.materialfingerprintapp.core.AppConfig.Server.GET_MATERIAL_IMAGE_URL_APPEND
import cz.cas.utia.materialfingerprintapp.core.AppConfig.Server.MATERIALS_URL
import cz.cas.utia.materialfingerprintapp.core.ui.components.ErrorScreen
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialImage

@Composable
fun BrowseLocalMaterialsScreen(
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToPolarPlotVisualisationScreen: (Boolean, Long, String, Boolean?, Long?, String?) -> Unit,
    navigateToAnalyticsHomeScreen: () -> Unit,
    viewModel: BrowseLocalMaterialsViewModel = hiltViewModel()
    /**
     * todo:
     * udelat RootMaterialScreenComposable (BrowseMaterialsScreenRoot) anebo odkud sem dát hiltViewModel,
     * abych pak mohl dávat uz jen onEvent funkci? - spis pro jine screeny to udelat,
     * tady jsem to nedelal protoze mam dva rooty - remote a local
     */
) {
    val state by viewModel.state.collectAsState()

    BrowseMaterialsScreenSuccessOrError(
        title = "Browse local materials",
        navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
        navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
        navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
        navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
        navigationEvents = viewModel.navigationEvents,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun BrowseRemoteMaterialsScreen(
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToPolarPlotVisualisationScreen: (Boolean, Long, String, Boolean?, Long?, String?) -> Unit,
    navigateToAnalyticsHomeScreen: () -> Unit,
    viewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    BrowseMaterialsScreenSuccessOrError(
        title = "Browse remote materials",
        navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
        navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
        navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
        navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
        navigationEvents = viewModel.navigationEvents,
        state = state,
        onEvent = viewModel::onEvent
    )
}
//todo dat ty similar materials screens definice do jineho souboru?
@Composable
fun BrowseSimilarLocalMaterialsScreen(
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToPolarPlotVisualisationScreen: (Boolean, Long, String, Boolean?, Long?, String?) -> Unit,
    navigateToAnalyticsHomeScreen: () -> Unit,
    viewModel: BrowseLocalMaterialsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    BrowseMaterialsScreenSuccessOrError(
        title = "Browse similar local materials",
        navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
        navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
        navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
        navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
        navigationEvents = viewModel.navigationEvents,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun BrowseSimilarRemoteMaterialsScreen(
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToPolarPlotVisualisationScreen: (Boolean, Long, String, Boolean?, Long?, String?) -> Unit,
    navigateToAnalyticsHomeScreen: () -> Unit,
    viewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    BrowseMaterialsScreenSuccessOrError(
        title = "Browse similar remote materials",
        navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
        navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
        navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
        navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
        navigationEvents = viewModel.navigationEvents,
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun BrowseMaterialsScreenSuccessOrError(
    title: String,
    navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit,
    navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit,
    navigateToPolarPlotVisualisationScreen: (Boolean, Long, String, Boolean?, Long?, String?) -> Unit,
    navigateToAnalyticsHomeScreen: () -> Unit,
    navigationEvents: SharedFlow<MaterialNavigationEvent>,
    state: MaterialsScreenState,
    onEvent: (MaterialEvent) -> Unit
) {
    NavigationHandler(
        navigationEventFlow = navigationEvents,
        navigate = { event ->
            when (event) {
                is MaterialNavigationEvent.ToBrowseSimilarLocalMaterialsScreen -> navigateToBrowseSimilarLocalMaterialsScreen(event.materialID)
                is MaterialNavigationEvent.ToBrowseSimilarRemoteMaterialsScreen -> navigateToBrowseSimilarRemoteMaterialsScreen(event.materialID)
                is MaterialNavigationEvent.ToPolarPlotVisualisationScreen -> navigateToPolarPlotVisualisationScreen(
                    event.isFirstMaterialSourceLocal,
                    event.firstMaterialId,
                    event.firstMaterialName,
                    event.isSecondMaterialSourceLocal,
                    event.secondMaterialId,
                    event.secondMaterialName
                )
                MaterialNavigationEvent.ToAnalyticsHomeScreen -> navigateToAnalyticsHomeScreen()
            }
        }
    )

    when (state) {
        is MaterialsScreenState.Success -> BrowseMaterialsScreen(
            title = title,
            state = state,
            onEvent = onEvent
        )

        is MaterialsScreenState.Error -> ErrorScreen(
            message = stringResource(id = state.messageResId),
            onAction = { onEvent(MaterialEvent.GoToAnalyticsHomeScreen) },
            buttonText = "Go to Analytics Home Screen",
            exception = state.exception
        )
    }
}

@Composable
fun BrowseMaterialsScreen(
    title: String,
    state: MaterialsScreenState.Success,
    onEvent: (MaterialEvent) -> Unit
) {
    Scaffold(
        topBar = {
            BackTopBarTitle(
                title = title,
                navigateBack = {} //todo later add navigation logic
            )
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(25.dp)
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
                onEvent = onEvent,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (state.isFindSimilarMaterialsDialogShown) {
                FindSimilarMaterialsDialog(
                    onDismissRequest = { onEvent(MaterialEvent.DismissFindSimilarMaterialsDialog) },
                    onLocalDatabaseButtonClick = { onEvent(MaterialEvent.FindSimilarLocalMaterials(state.getFirstCheckedMaterial())) },
                    onRemoteDatabaseButtonClick = { onEvent(MaterialEvent.FindSimilarRemoteMaterials(state.getFirstCheckedMaterial())) }
                )
            }
        }
    }
}

@Composable
fun SearchAndFilterSection(
    state: MaterialsScreenState.Success,
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
    state: MaterialsScreenState.Success,
    onEvent: (MaterialEvent) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = state.searchBarText,
        onValueChange = { searchText: String ->
            onEvent(MaterialEvent.SearchMaterials(searchText))
            },
        placeholder = { Text(text = "Search for name...") },
        modifier = modifier
    )
    //maybe put the search bar up the categories top down menu in case the top down menu is too wide
}

@Composable
fun CategoriesDropdownMenu(
    state: MaterialsScreenState.Success,
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
            onDropdownMenuClick = { onEvent(MaterialEvent.ShowDropdownMenu) },
            onDropdownMenuClosed = { onEvent(MaterialEvent.CloseDropdownMenu) }
        )
    }
}

@Composable
fun MaterialsListSection(
    state: MaterialsScreenState.Success,
    onEvent: (MaterialEvent) -> Unit,
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
    state: MaterialsScreenState.Success,
    onEvent: (MaterialEvent) -> Unit
) {
    val imageSize = 96.dp

    Text(text = material.name) //todo styling

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (material.photoThumbnail) {
            is MaterialImage.BitmapImage ->
                Image(
                    bitmap = material.photoThumbnail.imageBitmap,
                    contentDescription = "Material specular image",
                    contentScale = ContentScale.Crop, //todo nechat?
                    modifier = Modifier.size(imageSize) // todo adjust size so the polar plot is somehow readable
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

        Checkbox(
            checked = state.isMaterialChecked(material),
            onCheckedChange = { checked ->
                if (checked) {
                    onEvent(MaterialEvent.CheckMaterial(material))
                } else {
                    onEvent(MaterialEvent.UncheckMaterial(material))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End))
    }
}

@Composable
fun BottomButtonsSection(
    state: MaterialsScreenState.Success,
    onEvent: (MaterialEvent) -> Unit,
    modifier: Modifier) {
        Button(
            modifier = modifier,
            enabled = state.isFindSimilarMaterialButtonEnabled,
            onClick = { onEvent(MaterialEvent.FindSimilarMaterial) }) {
                Text(text = "Find similar material")
        }

        Button(
            modifier = modifier,
            enabled = state.isCreatePolarPlotButtonEnabled,
            onClick = { onEvent(MaterialEvent.CreatePolarPlot) }) {
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