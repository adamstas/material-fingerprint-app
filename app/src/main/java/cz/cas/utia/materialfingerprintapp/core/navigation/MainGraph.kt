package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse.BrowseLocalMaterialsViewModel
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse.BrowseMaterialsScreenRoot
import cz.cas.utia.materialfingerprintapp.features.analytics.presentation.browse.BrowseRemoteMaterialsViewModel
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
    // these functions are used a lot so to reduce code i defined them only once
    val navigateToBrowseSimilarLocalMaterialsScreen: (Long) -> Unit = { materialId: Long ->
        navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = materialId))
    }

    val navigateToBrowseSimilarRemoteMaterialsScreen: (Long) -> Unit = { materialId: Long ->
        navController.navigate(Screen.BrowseSimilarRemoteMaterials(materialId = materialId))
    }

    val navigateToPolarPlotVisualisationScreen: (
        isFirstMaterialSourceLocal: Boolean,
        firstMaterialId: Long,
        firstMaterialName: String,
        isSecondMaterialSourceLocal: Boolean?,
        secondMaterialId: Long?,
        secondMaterialName: String?
    ) -> Unit = { isFirstMaterialSourceLocal,
                  firstMaterialId,
                  firstMaterialName,
                  isSecondMaterialSourceLocal,
                  secondMaterialId,
                  secondMaterialName ->
        navController.navigate(
            Screen.PolarPlotVisualisation(
                isFirstMaterialSourceLocal = isFirstMaterialSourceLocal,
                firstMaterialId = firstMaterialId,
                firstMaterialName = firstMaterialName,
                isSecondMaterialSourceLocal = isSecondMaterialSourceLocal,
                secondMaterialId = secondMaterialId,
                secondMaterialName = secondMaterialName
            )
        )
    }

    val navigateToAnalyticsHomeScreen: () -> Unit = {
        navController.navigate(Screen.AnalyticsHomeScreen)
    }

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
            val viewModel: BrowseLocalMaterialsViewModel = hiltViewModel()
            /* view models are created here because then there can be just single BrowseMaterialsScreenRoot() whereas before
            there were 4 composables and each one was different just because it had different ViewModel
            and title and all navigation functions were still passed which was lots of boiler plate code */

            BrowseMaterialsScreenRoot(
                navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                viewModel = viewModel,
                title = "Browse local materials"
            )
        }

        composable<Screen.BrowseRemoteMaterials> {
            val viewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()

            BrowseMaterialsScreenRoot(
                navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                viewModel = viewModel,
                title = "Browse remote materials"
            )
        }

        composable<Screen.ApplyFilter> {
            ApplyFilterScreenRoot(
                navigateBack = { /* todo */ },
                navigateToBrowseSimilarLocalMaterialsScreen = { navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = -1L)) },
                navigateToBrowseSimilarRemoteMaterialsScreen = { navController.navigate(Screen.BrowseSimilarRemoteMaterials(materialId = -1L)) }
            )
        }

        composable<Screen.BrowseSimilarLocalMaterials> {
            val viewModel: BrowseLocalMaterialsViewModel = hiltViewModel()

            BrowseMaterialsScreenRoot(
                navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                viewModel = viewModel,
                title = "Browse similar local materials"
            )
        }

        composable<Screen.BrowseSimilarRemoteMaterials> {
            val viewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()

            BrowseMaterialsScreenRoot(
                navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                viewModel = viewModel,
                title = "Browse similar remote materials"
            )
        }

        composable<Screen.PolarPlotVisualisation> {
            PolarPlotVisualisationScreenRoot(
                navigateBack = { /*TODO*/ },
                navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                navigateToApplyFilterScreen = { navController.navigate(Screen.ApplyFilter(loadCharacteristicsFromStorage = true)) }
            )
        }

        composable<Screen.PhotosSummary> {
            PhotosSummaryScreenRoot(
                //pop current (PhotoSummary) screen from the backstack but save its state
                navigateBack = { navController.popBackStack<CameraScreen>(inclusive = false, saveState = true) },
                //todo kdyz uzivatel zmackne back na spodni liste, tak to udela obycejny popUp a cely state se smaze.. mozna teda vymazat cely backstack a nechat jen ulozeny state?

                navigateToPolarPlotVisualisationScreen = {
                        firstMaterialId: Long,
                        firstMaterialName: String -> navController.navigate(Screen.PolarPlotVisualisation(
                    isFirstMaterialSourceLocal = true,
                    firstMaterialId = firstMaterialId,
                    firstMaterialName = firstMaterialName,
                    isSecondMaterialSourceLocal = null,
                    secondMaterialId = null,
                    secondMaterialName = null
                ))
                }
            )
        }
    }
}
