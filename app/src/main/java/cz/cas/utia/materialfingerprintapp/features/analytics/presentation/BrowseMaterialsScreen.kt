package cz.cas.utia.materialfingerprintapp.features.analytics.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import cz.cas.utia.materialfingerprintapp.R
import cz.cas.utia.materialfingerprintapp.core.ui.components.BackTopBarTitle
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomSpacer
import cz.cas.utia.materialfingerprintapp.core.ui.theme.custom.CustomAppTheme
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialCategory

@Composable
fun BrowseMaterialsScreen() {
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
                SearchAndFilterSection()

                CustomSpacer()

                MaterialsListSection(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                )

                BottomButtonsSection(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
    }
}

@Composable
fun SearchAndFilterSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MaterialsSearchBar()
        CategoriesDropdownMenu()
    }
}

@Composable
fun MaterialsSearchBar() {
    //todo finish this
    Text("I will be a search bar")
    //maybe put the search bar up the categories top down menu in case the top down menu is too wide
}

@Composable
fun CategoriesDropdownMenu() {
    DropdownMenuWithCheckboxes(
        label = "Categories",
        options = listOf( //todo from state all categories (create automatically)
            DropDownMenuWithCheckboxesItem("FABRIC", 0),
            DropDownMenuWithCheckboxesItem("WOOD", 1),
            DropDownMenuWithCheckboxesItem("LEATHER", 2)
        ),
    )
}

@Composable
fun MaterialsListSection(modifier: Modifier) {
    var materials = remember { mutableStateListOf<MaterialUIElement>() }

    //init for now
    materials.add(MaterialUIElement(
        Material(
            id = 1,
            serverId = 1,
            name = "Leather1",
            photoPath = R.drawable.latka,
            fingerprintPath = R.drawable.latkapolarplot,
            category = MaterialCategory.LEATHER,
            statBrightness = 0.85,
            statColorVibrancy = 0.75,
            statHardness = 0.10,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.3,
            statMulticolored = 0.1,
            statNaturalness = 0.95,
            statPatternComplexity = 0.1,
            statScaleOfPattern = 0.2,
            statShininess = 0.9,
            statSparkle = 0.5,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.1,
            statThickness = 0.15,
            statValue = 90.0,
            statWarmth = 0.7
        ),
        checked = false
    ))
    materials.add(
        MaterialUIElement(
        Material(
            id = 2,
            serverId = 2,
            name = "Leather2",
            photoPath = R.drawable.latka2,
            fingerprintPath = R.drawable.latka2polarplot,
            category = MaterialCategory.LEATHER,
            statBrightness = 0.5,
            statColorVibrancy = 0.4,
            statHardness = 0.9,
            statCheckeredPattern = 0.0,
            statMovementEffect = 0.0,
            statMulticolored = 0.1,
            statNaturalness = 0.95,
            statPatternComplexity = 0.2,
            statScaleOfPattern = 0.6,
            statShininess = 0.3,
            statSparkle = 0.0,
            statStripedPattern = 0.0,
            statSurfaceRoughness = 0.8,
            statThickness = 0.9,
            statValue = 60.0,
            statWarmth = 0.8
        ), checked = true)
    )
    materials.add(
        MaterialUIElement(
            Material(
                id = 3,
                serverId = 3,
                name = "Leather3",
                photoPath = R.drawable.latka3,
                fingerprintPath = R.drawable.latka3polarplot,
                category = MaterialCategory.LEATHER,
                statBrightness = 0.5,
                statColorVibrancy = 0.4,
                statHardness = 0.9,
                statCheckeredPattern = 0.0,
                statMovementEffect = 0.08,
                statMulticolored = 0.51,
                statNaturalness = 0.95,
                statPatternComplexity = 0.2,
                statScaleOfPattern = 0.6,
                statShininess = 0.3,
                statSparkle = 0.0,
                statStripedPattern = 0.0,
                statSurfaceRoughness = 0.41,
                statThickness = 0.9,
                statValue = 60.0,
                statWarmth = 0.8
            ), checked = false)
    )
    materials.add(
        MaterialUIElement(
            Material(
                id = 4,
                serverId = 4,
                name = "Leather4",
                photoPath = R.drawable.latka4,
                fingerprintPath = R.drawable.latka4polarplot,
                category = MaterialCategory.LEATHER,
                statBrightness = 0.99,
                statColorVibrancy = 0.4,
                statHardness = 0.9,
                statCheckeredPattern = 0.0,
                statMovementEffect = 0.08,
                statMulticolored = 0.51,
                statNaturalness = 0.95,
                statPatternComplexity = 0.2,
                statScaleOfPattern = 0.6,
                statShininess = 0.3,
                statSparkle = 0.0,
                statStripedPattern = 0.0,
                statSurfaceRoughness = 0.41,
                statThickness = 0.9,
                statValue = 60.0,
                statWarmth = 0.8
            ), checked = false)
    )

    LazyColumn(modifier = modifier)
     {
        items(materials) { material ->
            MaterialListRow(materialUIElement = material)
            CustomHorizontalDivider()
        }
    }
}

@Composable
fun MaterialListRow(materialUIElement: MaterialUIElement) {
        Text(text = materialUIElement.material.name) //todo styling

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = materialUIElement.material.photoPath),
                contentDescription = "Latka Image", //todo zmenit
                contentScale = ContentScale.Crop, //todo nechat?
                modifier = Modifier.size(96.dp) // todo adjust size so the polar plot is somehow readable
            )

            Spacer(modifier = Modifier.width(24.dp))

            Image( //polar plots wont have axes names so this should be enough size..or make bigger?
                painter = painterResource(id = materialUIElement.material.fingerprintPath),
                contentDescription = "Latka fingerprint image",
                contentScale = ContentScale.Crop, //todo nechat?
                modifier = Modifier.size(96.dp)  // todo adjust size so the polar plot is somehow readable
            )

            Checkbox(
                checked = materialUIElement.checked,
                onCheckedChange = {
                    materialUIElement.checked = !materialUIElement.checked
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.End))
        }
}

@Composable
fun BottomButtonsSection(modifier: Modifier) {
        Button(
            modifier = modifier,
            onClick = { /*TODO*/ }) {
                Text(text = "Find similar material")
        }

        Button(
            modifier = modifier,
            onClick = { /*TODO*/ }) {
                Text(text = "Create polar plot")
        }

}

//todo finish this screen just with dummy data and then finish the database

//todo move to core UI elements package
data class DropDownMenuWithCheckboxesItem(
    val text: String,
    val id: Int
)

//todo move to core UI elements package
//inspired by https://stackoverflow.com/questions/75397960/jetpack-compose-combo-box-with-dropdown //todo nechat? zminit?
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuWithCheckboxes(
    label: String,
    options: List<DropDownMenuWithCheckboxesItem>,
    //onOptionsChosen: (List<ComboOption>) -> Unit,
    modifier: Modifier = Modifier, //todo nechat?
    selectedIds: List<Int> = emptyList(), //todo load from state
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionsList = remember { mutableStateListOf<Int>()}

    //load selected categories
    selectedIds.forEach{
        selectedOptionsList.add(it)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
            if (!expanded) {
               // onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
                //todo jakmile zavre menu, tato logika se provede s temi zaskrtnutymi (predat funkci teto composable, at je to znovupouzitelne)
                //zaskrtnute ziskat nejak takto: options.filter { it.id in selectedOptionsList }.toList()
            }

        },
        modifier = Modifier.width(190.dp), //todo keep hardcoded?
    ) {
        val selectedSummary: String = when (selectedOptionsList.size) {
            0 -> "None selected"
            1 -> options.first { it.id == selectedOptionsList.first() }.text
            else -> "Selected ${selectedOptionsList.size}"
        }
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = selectedSummary,
            onValueChange = {},
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                //onOptionsChosen(options.filter { it.id in selectedOptionsList }.toList())
                //todo jakmile zavre menu, tato logika se provede s temi zaskrtnutymi (predat funkci teto composable, at je to znovupouzitelne)
                //ale mozna tahle logika staci jen výš, otestovat
                //zaskrtnute ziskat nejak takto: options.filter { it.id in selectedOptionsList }.toList()
            },
        ) {
            for (option in options) {
                var checked = remember {
                    derivedStateOf{option.id in selectedOptionsList}
                }.value

                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = checked,
                                onCheckedChange = { newCheckedState ->
                                    if (newCheckedState) {
                                        selectedOptionsList.add(option.id)
                                    } else {
                                        selectedOptionsList.remove(option.id)
                                    }
                                },
                            )
                            Text(text = option.text)
                        }
                    },
                    onClick = {
                        if (!checked) {
                            selectedOptionsList.add(option.id)
                        } else {
                            selectedOptionsList.remove(option.id)
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


@Preview
@Composable
fun BrowseMaterialsScreenPreview() {
    CustomAppTheme {
        BrowseMaterialsScreen()
    }
}