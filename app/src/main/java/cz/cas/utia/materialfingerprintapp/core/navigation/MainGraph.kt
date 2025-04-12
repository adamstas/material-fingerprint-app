package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse.BrowseLocalMaterialsViewModel
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse.BrowseMaterialsScreenRoot
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.browse.BrowseRemoteMaterialsViewModel
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.filter.ApplyFilterScreenRoot
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.home.AnalysisHomeScreenRoot
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.visualise.PolarPlotVisualisationScreenRoot
import cz.cas.utia.materialfingerprintapp.features.capturing.presentation.capturing.CapturingScreenRoot
import cz.cas.utia.materialfingerprintapp.features.capturing.presentation.photossummary.PhotosSummaryScreenRoot
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.SettingsScreenRoot
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial.TutorialScreenRoot

@Composable
fun MainGraph(
    navController: NavHostController,
    viewModel: MainGraphViewModel = hiltViewModel()
   // modifier: Modifier
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

    val startDestination = defaultStartDestination
    if (startDestination != null) { // start destination is null just for a really short while so user does not notice at all
        NavHost(
            navController = navController,
            startDestination = startDestination
           // modifier = modifier
        ) {

            composable<Screen.Settings> {
                SettingsScreenRoot(
                    navigateToTutorialScreen = { navController.navigate(Screen.Tutorial) }
                )
            }

            composable<Screen.Capturing> {
                CapturingScreenRoot(
                    navigateToPhotosSummaryScreen = {
                        navController.navigate(Screen.PhotosSummary)
                        {
                            restoreState = true
                        }
                    }

                )
            }

            composable<Screen.AnalysisHome> {
                AnalysisHomeScreenRoot(
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
                    navigateBack = { navController.popBackStack() },
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    viewModel = screenViewModel,
                    title = "Browse local materials"
                )
            }

            composable<Screen.BrowseRemoteMaterials> {
                val screenViewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()

                BrowseMaterialsScreenRoot(
                    navigateBack = { navController.popBackStack() },
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    viewModel = screenViewModel,
                    title = "Browse remote materials"
                )
            }

            composable<Screen.ApplyFilter> {
                ApplyFilterScreenRoot(
                    navigateBack = { navController.popBackStack() },
                    navigateToBrowseSimilarLocalMaterialsScreen = { navController.navigate(Screen.BrowseSimilarLocalMaterials(materialId = -1L)) },
                    navigateToBrowseSimilarRemoteMaterialsScreen = { navController.navigate(Screen.BrowseSimilarRemoteMaterials(materialId = -1L)) }
                )
            }

            composable<Screen.BrowseSimilarLocalMaterials> {
                val screenViewModel: BrowseLocalMaterialsViewModel = hiltViewModel()

                BrowseMaterialsScreenRoot(
                    navigateBack = { navController.popBackStack() },
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    viewModel = screenViewModel,
                    title = "Browse similar local materials"
                )
            }

            composable<Screen.BrowseSimilarRemoteMaterials> {
                val screenViewModel: BrowseRemoteMaterialsViewModel = hiltViewModel()

                BrowseMaterialsScreenRoot(
                    navigateBack = { navController.popBackStack() },
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToPolarPlotVisualisationScreen = navigateToPolarPlotVisualisationScreen,
                    viewModel = screenViewModel,
                    title = "Browse similar remote materials"
                )
            }

            composable<Screen.PolarPlotVisualisation> {
                PolarPlotVisualisationScreenRoot(
                    navigateBack = { navController.popBackStack() },
                    navigateToBrowseSimilarLocalMaterialsScreen = navigateToBrowseSimilarLocalMaterialsScreen,
                    navigateToBrowseSimilarRemoteMaterialsScreen = navigateToBrowseSimilarRemoteMaterialsScreen,
                    navigateToApplyFilterScreen = { navController.navigate(Screen.ApplyFilter(loadCharacteristicsFromStorage = true)) }
                )
            }

            composable<Screen.PhotosSummary> {
                PhotosSummaryScreenRoot(
                   navigateBack =  {
                       val popped = navController.popBackStack(
                           route = Screen.Capturing,
                           inclusive = false,
                           saveState = true
                       )
                       if (!popped) { // prevents bug when Camera screen is not in nav back stack
                           navController.navigate(Screen.Capturing) {
                               launchSingleTop = true
                               restoreState = true
                           }
                       }
                   },

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

            composable<Screen.Tutorial> {
                TutorialScreenRoot(
                    onTutorialCompleted = { navController.popBackStack() }
                )
            }
        }
    }
}
