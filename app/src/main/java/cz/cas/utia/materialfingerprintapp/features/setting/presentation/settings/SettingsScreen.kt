package cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.core.ui.components.NavigationHandler
import cz.cas.utia.materialfingerprintapp.core.ui.components.BasicDropdownMenu
import cz.cas.utia.materialfingerprintapp.core.ui.components.CustomHorizontalDivider
import cz.cas.utia.materialfingerprintapp.core.ui.components.TopBarTitle

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateToTutorialScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    NavigationHandler(
        navigationEventFlow = viewModel.navigationEvents,
        navigate = { event ->
            when (event) {
                SettingsNavigationEvent.ToTutorialScreen -> navigateToTutorialScreen()
            }
        }
    )

    SettingsScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun SettingsScreen(
    state: SettingsScreenState,
    onEvent: (SettingsEvent) -> Unit
) {
    // creates launcher of Android dialog for storing files
    val createCsvFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? -> // returns URI of the created file, otherwise null (if user exited the dialog etc.)

        // user selected some path in his filesystem = URI is not null and we can actually store the file
        if (uri != null) {
            onEvent(SettingsEvent.ExportLocalMaterialsAsCsv(uri))
        } else {
            onEvent(SettingsEvent.SetCsvExportStatusAsNotStarted)
        }
    }

    val createZipFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/zip")
    ) { uri: Uri? ->

        if (uri != null) {
            onEvent(SettingsEvent.ExportLocalMaterialImagesAsZip(uri))
        } else {
            onEvent(SettingsEvent.SetZipExportStatusAsNotStarted)
        }
    }

    LaunchedEffect(state.exportMaterialsReady) {
        if (state.exportMaterialsReady)
            createCsvFileLauncher.launch(getCurrentMaterialsCsvExportFileName())
    }

    LaunchedEffect(state.exportImagesReady) {
        if (state.exportImagesReady)
            createZipFileLauncher.launch(getCurrentMaterialsZipExportFileName())
    }

    val settingScreenItems = listOf(
        SettingsItemData(
            text = "Store data on the server\n" +
                    "(anonymously)",
            content = {
                Switch(
                    checked = state.isStoreDataOnServerSwitchChecked,
                    onCheckedChange = { newSwitchValue ->
                        onEvent(SettingsEvent.SwitchStoreDataOnServerSwitch(newSwitchValue = newSwitchValue)) })
            }),

        SettingsItemData(
            text = "Server URL\n",
            content = {
                
                Column(modifier = Modifier.fillMaxWidth(0.95f)) {

                    TextField(
                        value = state.serverUrl,
                        onValueChange = { url: String ->
                            onEvent(SettingsEvent.SetServerUrl(url))
                        },
                        placeholder = { Text(text = "e.g. https://myserver.com:8000") },
                        singleLine = true,
                        isError = !state.isServerUrlValid,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(top = 4.dp))

                    if (!state.isServerUrlValid) {
                        Text(
                            text = "Invalid URL format",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )

                    } else {
                        Text(
                            text = "Change this only if you run your own server.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }),

        SettingsItemData(
            text = "Export local materials\n" +
                    "as CSV without images",
            content = {
                Button(
                    onClick = { onEvent(SettingsEvent.CheckIfAnyMaterialsToExport) },
                    enabled = state.materialCsvExportStatus != MaterialExportStatus.IN_PROGRESS
                ) {
                    Text(
                        when(state.materialCsvExportStatus) {
                            MaterialExportStatus.NOT_STARTED -> "Export as CSV"
                            MaterialExportStatus.IN_PROGRESS -> "Export in progress..."
                            MaterialExportStatus.FINISHED -> "Export done " + "\u2705" // green tick
                            MaterialExportStatus.NOTHING_TO_EXPORT -> "Nothing to export!"
                        }
                    )
                }
            }),

        SettingsItemData(
            text = "Export local material\n" +
                    "images as ZIP",
            content = {
                Button(
                    onClick = { onEvent(SettingsEvent.CheckIfAnyImagesToExport) },
                    enabled = state.materialZipExportStatus != MaterialExportStatus.IN_PROGRESS
                ) {
                    Text(
                        when(state.materialZipExportStatus) {
                            MaterialExportStatus.NOT_STARTED -> "Export images as ZIP"
                            MaterialExportStatus.IN_PROGRESS -> "Export in progress..."
                            MaterialExportStatus.FINISHED -> "Export done " + "\u2705" // green tick
                            MaterialExportStatus.NOTHING_TO_EXPORT -> "Nothing to export!"
                        }
                    )
                }
            }),

        SettingsItemData(
            text = "Default screen",
            content = {
                BasicDropdownMenu(
                    expanded = state.isDefaultScreenDropdownMenuExpanded,
                    onDropdownMenuClick = { onEvent(SettingsEvent.ShowDropdownMenu) },
                    onDropdownMenuClosed = { onEvent(SettingsEvent.CloseDropdownMenu) },
                    onDropdownMenuItemClick = { defaultScreen: DefaultScreen ->
                        onEvent(SettingsEvent.SelectDefaultScreen(defaultScreen))
                    },
                    options = DefaultScreen.entries,
                    selectedOption = state.selectedDefaultScreen,
                    modifier = Modifier.width(172.dp)
                )
            }),

        SettingsItemData(
            text = "Replay tutorial",
            content = {
                Button(
                    onClick = { onEvent(SettingsEvent.ReplayTutorial) }
                ) {
                    Text(text = "Tutorial")
                }
            })
    )

    Scaffold(
        topBar = {
            TopBarTitle(title = "Settings")
            }
        )

    { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            Modifier
                .padding(paddingValues)
                .padding(24.dp)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(scrollState)
        )
        {
            settingScreenItems.forEach { item ->
                SettingsScreenItem(
                    text = item.text,
                    content = item.content)

            CustomHorizontalDivider()
            }

            Text(
                text = "Settings on this screen are saved automatically.",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            AboutSection()
        }
    }
}

@Composable
fun SettingsScreenItem(
    text: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text)
        content()
    }
}

@Composable
fun AboutSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "About",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "MatTag - App for Material Fingerprinting",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Developed by Adam Stas in cooperation with Veronika Vilimovska and Daniel Pilar",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Institute of Information Theory and Automation of the CAS",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "This application was created as a diploma thesis at the Czech Technical University in Prague, Faculty of Information Technology",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = "Copyright (c) 2025 Adam Stas.\n" +
                            "This software is licensed under the MIT License.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Version: 1.1",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}