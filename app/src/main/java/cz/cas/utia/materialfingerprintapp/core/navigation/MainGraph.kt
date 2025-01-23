package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse.BrowseLocalMaterialsScreen
import cz.cas.utia.materialfingerprintapp.features.camera.presentation.camera.CameraScreenRoot
import cz.cas.utia.materialfingerprintapp.features.camera.presentation.photossummary.PhotosSummaryScreenRoot
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.SettingsScreen

@Composable
fun MainGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = SettingsScreens, //todo udelat nejak z nastaveni nastavitelny asi
        //modifier = modifier
    ) {

        composable<SettingsScreens> {
            SettingsScreen()
        }

        composable<CameraScreens> {
            CameraScreenRoot(
                navigateToPhotosSummaryScreen = { navController.navigate(Screen.PhotosSummary) }
            )
        }

        composable<AnalyticsScreens> {
            BrowseLocalMaterialsScreen()
            //BrowseRemoteMaterialsScreen() //todo test this screen when i have working remote repository
            //here will be another screen from which user goes to browse local / browse remote / filter..
        }

        composable<Screen.PhotosSummary> {
            PhotosSummaryScreenRoot(
                navigateBack = { navController.popBackStack() }
            )
        }
    }
}
