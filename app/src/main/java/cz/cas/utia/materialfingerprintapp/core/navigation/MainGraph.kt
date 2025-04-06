package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.SettingsScreenRoot

@Composable
fun MainGraph(
    navController: NavHostController,
    viewModel: MainGraphViewModel = hiltViewModel()
) {
    val defaultStartDestination by viewModel.defaultStartDestination.collectAsState()

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
        navController.navigate(Screen.AnalyticsHome)
    }

    val startDestination = defaultStartDestination
    if (startDestination != null) { // start destination is null just for a really short while so user does not notice at all
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {

            composable<Screen.Settings> {
                SettingsScreenRoot(
                    navigateToTutorialScreen = {/*todo*/}
                )
            }

            composable<Screen.Camera> {
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

            composable<Screen.AnalyticsHome> {
                AnalyticsHomeScreenRoot(
                    navigateToBrowseLocalMaterialsScreen = { navController.navigate(Screen.BrowseLocalMaterials) },
                    navigateToBrowseRemoteMaterialsScreen = { navController.navigate(Screen.BrowseRemoteMaterials) },
                    navigateToApplyFilterScreen = { navController.navigate(Screen.ApplyFilter(loadCharacteristicsFromStorage = false)) }
                )
            }

            composable<Screen.BrowseLocalMaterials> {
                val screenViewModel: BrowseLocalMaterialsViewModel = hiltViewModel()
                /* view models are created here because then there can be just single BrowseMaterialsScreenRoot() whereas before
                there were 4 composables and each one was different just because it had different ViewModel
                and title and all navigation functions were still passed which was lots of boiler plate code */

                BrowseMaterialsScreenRoot(
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                    viewModel = screenViewModel,
                    title = "Browse local materials"
                )
            }

            composable<Screen.BrowseRemoteMaterials> {
                val screenViewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()

                BrowseMaterialsScreenRoot(
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                    viewModel = screenViewModel,
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
                val screenViewModel: BrowseLocalMaterialsViewModel = hiltViewModel()

                BrowseMaterialsScreenRoot(
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                    viewModel = screenViewModel,
                    title = "Browse similar local materials"
                )
            }

            composable<Screen.BrowseSimilarRemoteMaterials> {
                val screenViewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()

                BrowseMaterialsScreenRoot(
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    navigateToAnalyticsHomeScreen = navigateToAnalyticsHomeScreen,
                    viewModel = screenViewModel,
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
                    navigateBack = { navController.popBackStack<Screen.Camera>(inclusive = false, saveState = true) },
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
}
