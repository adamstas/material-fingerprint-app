package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse.BrowseLocalMaterialsScreen
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse.BrowseSimilarLocalMaterialsScreen
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.filter.ApplyFilterScreenRoot
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.home.AnalyticsHomeScreenRoot
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.visualise.PolarPlotVisualisationScreenRoot
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

        composable<Screen.AnalyticsHomeScreen> {
            AnalyticsHomeScreenRoot(
                navigateToBrowseLocalMaterialsScreen = { navController.navigate(Screen.BrowseLocalMaterials) },
                navigateToBrowseRemoteMaterialsScreen = { navController.navigate(Screen.BrowseRemoteMaterials) },
                navigateToApplyFilterScreen = { navController.navigate(Screen.ApplyFilter(loadCharacteristicsFromStorage = false)) }
            )
        }

        composable<Screen.BrowseLocalMaterials> {
            BrowseLocalMaterialsScreen(
                navigateToBrowseSimilarLocalMaterialsScreen = { materialId: Long ->
                    navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = materialId)) },
                navigateToBrowseSimilarRemoteMaterialsScreen = { materialId: Long ->
                    navController.navigate(Screen.BrowseSimilarRemoteMaterials(materialId = materialId)) },
                navigateToPolarPlotVisualisationScreen = {
                        isFirstMaterialSourceLocal: Boolean,
                        firstMaterialId: Long,
                        isSecondMaterialSourceLocal: Boolean?,
                        secondMaterialId: Long? -> navController.navigate(Screen.PolarPlotVisualisation(
                    isFirstMaterialSourceLocal = isFirstMaterialSourceLocal,
                    firstMaterialId = firstMaterialId,
                    isSecondMaterialSourceLocal = isSecondMaterialSourceLocal,
                    secondMaterialId = secondMaterialId
                ))
                }
            )
            //BrowseRemoteMaterialsScreen() //todo test this screen when i have working remote repository
            //here will be another screen from which user goes to browse local / browse remote / filter..
        }

        composable<Screen.BrowseRemoteMaterials> {
            //todo az bude ready remote repository tak to sem pridat
        }

        composable<Screen.ApplyFilter> {
            ApplyFilterScreenRoot(
                navigateBack = { /* todo */ },
                navigateToBrowseSimilarLocalMaterialsScreen = { navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = -1L)) },
                navigateToBrowseSimilarRemoteMaterialsScreen = { navController.navigate(Screen.BrowseSimilarRemoteMaterials(materialId = -1L)) }
            )
        }

        composable<Screen.BrowseSimilarLocalMaterials> {
            BrowseSimilarLocalMaterialsScreen(
                navigateToBrowseSimilarLocalMaterialsScreen = { materialId: Long ->
                    navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = materialId)) },
                navigateToBrowseSimilarRemoteMaterialsScreen = { materialId: Long ->
                    navController.navigate(Screen.BrowseSimilarRemoteMaterials(materialId = materialId)) },
                navigateToPolarPlotVisualisationScreen = {
                        isFirstMaterialSourceLocal: Boolean,
                        firstMaterialId: Long,
                        isSecondMaterialSourceLocal: Boolean?,
                        secondMaterialId: Long? -> navController.navigate(Screen.PolarPlotVisualisation(
                    isFirstMaterialSourceLocal = isFirstMaterialSourceLocal,
                    firstMaterialId = firstMaterialId,
                    isSecondMaterialSourceLocal = isSecondMaterialSourceLocal,
                    secondMaterialId = secondMaterialId
                ))
                }
            )
        }

        composable<Screen.BrowseSimilarRemoteMaterials> {

        }

        composable<Screen.PolarPlotVisualisation> {
            PolarPlotVisualisationScreenRoot(
                navigateBack = { /*TODO*/ },
                navigateToBrowseSimilarLocalMaterialsScreen = { materialId ->
                    navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = materialId)) },
                navigateToBrowseSimilarRemoteMaterialsScreen = { /*TODO*/ },
                navigateToApplyFilterScreen = { navController.navigate(Screen.ApplyFilter(loadCharacteristicsFromStorage = true)) }
            )
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
