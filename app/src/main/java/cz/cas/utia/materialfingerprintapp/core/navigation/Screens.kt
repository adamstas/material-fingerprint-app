package cz.cas.utia.materialfingerprintapp.core.navigation

import cz.cas.utia.materialfingerprintapp.R
import kotlinx.serialization.Serializable

//fun capitalizeFirstLetter(input: String): String { //todo move this function to some helper functions section
//    if (input.isEmpty())
//        return input
//    return input.substring(0, 1).uppercase() + input.substring(1)
//}

@Serializable
object CameraScreen

@Serializable
object SettingsScreen

val mainScreens = listOf(
    Screen.MainScreen.Camera,
    Screen.MainScreen.Analytics,
    Screen.MainScreen.Settings
    )

sealed class Screen {

    @Serializable
    sealed class MainScreen<T>(val label: String, val iconSelectedId: Int, val iconUnselectedId: Int, val route: T) {
        @Serializable
        data object Camera: MainScreen<CameraScreen>(
            label = "Camera",
            iconSelectedId = R.drawable.photo_camera_filled,
            iconUnselectedId = R.drawable.photo_camera,
            route = CameraScreen
        )

        @Serializable
        data object Analytics: MainScreen<AnalyticsHomeScreen>(
            label = "Analytics",
            iconSelectedId = R.drawable.analytics_filled,
            iconUnselectedId = R.drawable.analytics,
            route = AnalyticsHomeScreen
        )

        @Serializable
        data object Settings: MainScreen<SettingsScreen>(
            label = "Settings",
            iconSelectedId = R.drawable.settings_filled,
            iconUnselectedId = R.drawable.settings,
            route = SettingsScreen
        )
    }

    @Serializable
    data object PhotosSummary: Screen()

    @Serializable
    data object AnalyticsHomeScreen: Screen()

    @Serializable
    data object BrowseLocalMaterials: Screen()

    @Serializable
    data object BrowseRemoteMaterials: Screen()

    @Serializable
    data class ApplyFilter(
        val loadCharacteristicsFromStorage: Boolean
    ): Screen()

    @Serializable
    data class PolarPlotVisualisation(
        val isFirstMaterialSourceLocal: Boolean,
        val firstMaterialId: Long,
        val isSecondMaterialSourceLocal: Boolean?,
        val secondMaterialId: Long?
    ): Screen()

    @Serializable
    data class BrowseSimilarLocalMaterials(val materialId: Long): Screen()

    @Serializable
    data class BrowseSimilarRemoteMaterials(val materialId: Long): Screen()
}


//TODO smazat pokud budu pouzivat novou navigaci
//@Serializable
//sealed class Screen() {
//    //here screens that are not from main navigation
//
//    @Serializable
//    sealed class MainNavigationScreen(
//        val route: Screen,
//        val title: String,
//        val iconSelectedId: Int,
//        val iconUnselectedId: Int
//    ): Screen() {
//
//
//        @Serializable
//        data object CameraScreen: MainNavigationScreen(
//            route = CameraScreen,
//            title = "Camera",
//            iconSelectedId = R.drawable.photo_camera_filled,
//            iconUnselectedId = R.drawable.photo_camera
//        )
//
//        @Serializable
//        data object AnalyticsScreen: MainNavigationScreen(
//            route = AnalyticsScreen,
//            title = "analytics",
//            iconSelectedId = R.drawable.analytics_filled,
//            iconUnselectedId = R.drawable.analytics
//        )
//
//        @Serializable
//        data object SettingsScreen: MainNavigationScreen(
//            route = SettingsScreen,
//            title = "settings",
//            iconSelectedId = R.drawable.settings_filled,
//            iconUnselectedId = R.drawable.settings
//        )
//    }
//}

//enum class BottomNavigation(val label: String, val iconSelectedId: Int, val iconUnselectedId: Int, val route: Screen) {
//    CAMERA("Camera", R.drawable.photo_camera_filled, R.drawable.photo_camera, Screen.MainNavigationScreen.CameraScreen),
//    ANALYTICS("Analytics", R.drawable.analytics_filled, R.drawable.analytics, Screen.MainNavigationScreen.AnalyticsScreen),
//    SETTINGS("Settings", R.drawable.settings_filled, R.drawable.settings, Screen.MainNavigationScreen.SettingsScreen)
//}

//val mainNavigationScreens: List<Screen.MainNavigationScreen> =
//    listOf(
//        Screen.MainNavigationScreen.CameraScreen,
//        Screen.MainNavigationScreen.AnalyticsScreen,
//        Screen.MainNavigationScreen.SettingsScreen
//    )