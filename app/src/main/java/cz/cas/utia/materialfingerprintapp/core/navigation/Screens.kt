package cz.cas.utia.materialfingerprintapp.core.navigation

import cz.cas.utia.materialfingerprintapp.R
import kotlinx.serialization.Serializable

enum class ScreenGroup {
    CAMERA,
    ANALYTICS,
    SETTINGS
}

val mainScreens = listOf(
    Screen.MainScreen.Camera,
    Screen.MainScreen.Analytics,
    Screen.MainScreen.Settings
    )

sealed class Screen {

    abstract val group: ScreenGroup

    // for proper bottom bar icons highlighting
    companion object {
        fun getGroupByClassName(className: String?): ScreenGroup? {
            return when (className) {
                Camera::class.qualifiedName -> ScreenGroup.CAMERA
                PhotosSummary::class.qualifiedName -> ScreenGroup.CAMERA
                AnalyticsHome::class.qualifiedName -> ScreenGroup.ANALYTICS
                BrowseLocalMaterials::class.qualifiedName -> ScreenGroup.ANALYTICS
                BrowseRemoteMaterials::class.qualifiedName -> ScreenGroup.ANALYTICS
                BrowseSimilarLocalMaterials::class.qualifiedName -> ScreenGroup.ANALYTICS
                BrowseSimilarRemoteMaterials::class.qualifiedName -> ScreenGroup.ANALYTICS
                PolarPlotVisualisation::class.qualifiedName -> ScreenGroup.ANALYTICS
                ApplyFilter::class.qualifiedName -> ScreenGroup.ANALYTICS
                Settings::class.qualifiedName -> ScreenGroup.SETTINGS
                else -> null
            }
        }
    }

    @Serializable
    sealed class MainScreen<T>(val label: String, val iconSelectedId: Int, val iconUnselectedId: Int, val route: T): Screen() {
        @Serializable
        data object Camera: MainScreen<Screen.Camera>(
            label = "Camera",
            iconSelectedId = R.drawable.photo_camera_filled,
            iconUnselectedId = R.drawable.photo_camera,
            route = Screen.Camera
        ) {
            override val group = ScreenGroup.CAMERA
        }

        @Serializable
        data object Analytics: MainScreen<AnalyticsHome>(
            label = "Analytics",
            iconSelectedId = R.drawable.analytics_filled,
            iconUnselectedId = R.drawable.analytics,
            route = AnalyticsHome
        ) {
            override val group = ScreenGroup.ANALYTICS
        }

        @Serializable
        data object Settings: MainScreen<Screen.Settings>(
            label = "Settings",
            iconSelectedId = R.drawable.settings_filled,
            iconUnselectedId = R.drawable.settings,
            route = Screen.Settings
        ) {
            override val group = ScreenGroup.SETTINGS
        }
    }

    @Serializable
    data object Camera: Screen() {
        override val group = ScreenGroup.CAMERA
    }

    @Serializable
    data object Settings: Screen() {
        override val group = ScreenGroup.SETTINGS
    }

    @Serializable
    data object AnalyticsHome: Screen() {
        override val group = ScreenGroup.ANALYTICS
    }

    @Serializable
    data object PhotosSummary: Screen() {
        override val group = ScreenGroup.CAMERA
    }

    @Serializable
    data object BrowseLocalMaterials: Screen() {
        override val group = ScreenGroup.ANALYTICS
    }

    @Serializable
    data object BrowseRemoteMaterials: Screen() {
        override val group = ScreenGroup.ANALYTICS
    }

    @Serializable
    data class ApplyFilter(
        val loadCharacteristicsFromStorage: Boolean
    ): Screen() {
        override val group = ScreenGroup.ANALYTICS
    }

    @Serializable
    data class PolarPlotVisualisation(
        val isFirstMaterialSourceLocal: Boolean,
        val firstMaterialId: Long,
        val firstMaterialName: String,
        val isSecondMaterialSourceLocal: Boolean?,
        val secondMaterialId: Long?,
        val secondMaterialName: String?
    ): Screen() {
        override val group = ScreenGroup.ANALYTICS
    }

    @Serializable
    data class BrowseSimilarLocalMaterials(val materialId: Long): Screen() {
        override val group = ScreenGroup.ANALYTICS
    }

    @Serializable
    data class BrowseSimilarRemoteMaterials(val materialId: Long): Screen() {
        override val group = ScreenGroup.ANALYTICS
    }
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