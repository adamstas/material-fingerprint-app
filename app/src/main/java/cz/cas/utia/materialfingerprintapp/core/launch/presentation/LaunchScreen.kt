package cz.cas.utia.materialfingerprintapp.core.launch.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import cz.cas.utia.materialfingerprintapp.core.navigation.MainNavigation
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.permission.PermissionScreenRoot
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial.TutorialScreenRoot

@Composable
fun LaunchScreenRoot(
    viewModel: LaunchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    when (state) {
        LaunchScreenState.LaunchMainContent -> MainNavigation()
        LaunchScreenState.Loading -> {}
        LaunchScreenState.ShowPermissionsScreen -> PermissionScreenRoot(
            onAllPermissionsGranted = { viewModel.onEvent(LaunchEvent.PermissionsGranted) })
        
        LaunchScreenState.ShowTutorial -> TutorialScreenRoot(
            onTutorialCompleted = { viewModel.onEvent(LaunchEvent.TutorialCompleted) })
    }
}