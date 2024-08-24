package cz.cas.utia.materialfingerprintapp.core.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            val bulyn = false
            fun bulyn2(boolean: Boolean): Unit {

            }
            SettingsScreen()
        }

        composable<CameraScreens> {
            Text(text = "navigacka_camera")
        }

        composable<AnalyticsScreens> {
            Text(text = "navigacka_analytics")
        }
    }
}