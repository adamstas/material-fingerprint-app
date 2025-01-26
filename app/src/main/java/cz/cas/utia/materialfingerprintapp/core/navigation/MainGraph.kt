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
        startDestination = CameraScreen, //todo udelat nejak z nastaveni nastavitelny asi
        //modifier = modifier
    ) {

        composable<SettingsScreen> {
            SettingsScreen()
        }

        composable<CameraScreen> {
            CameraScreenRoot(
                navigateToPhotosSummaryScreen = {
                    navController.navigate(Screen.PhotosSummary)
                    {
                        //todo remove later when navigation is clear to me
                            // popUpTo(Screen.PhotosSummary) { //if PhotosSummary is in backstack then it will pop up everything except this PhotosSummary; otherwise nothing is popped up
                            // saveState = true
                            //  }
                            // launchSingleTop = true
                        restoreState = true
                    }
                }

            )
        }

        composable<AnalyticsScreen> {
            BrowseLocalMaterialsScreen()
            //BrowseRemoteMaterialsScreen() //todo test this screen when i have working remote repository
            //here will be another screen from which user goes to browse local / browse remote / filter..
        }

        composable<Screen.PhotosSummary> {
            PhotosSummaryScreenRoot(
                //pop current (PhotoSummary) screen from the backstack but save its state
                navigateBack = { navController.popBackStack<CameraScreen>(inclusive = false, saveState = true) }
                //todo kdyz uzivatel zmackne back na spodni liste, tak to udela obycejny popUp a cely state se smaze.. mozna teda vymazat cely backstack a nechat jen ulozeny state?
            )
        }
    }
}
