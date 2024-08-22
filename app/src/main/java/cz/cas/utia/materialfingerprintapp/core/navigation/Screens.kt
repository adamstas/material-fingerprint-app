package cz.cas.utia.materialfingerprintapp.core.navigation

import cz.cas.utia.materialfingerprintapp.R

fun capitalizeFirstLetter(input: String): String { //todo move this function to some helper functions section
    if (input.isEmpty())
        return input
    return input.substring(0, 1).uppercase() + input.substring(1)
}

sealed class Screen(val route: String) {
    //here screens that are not from main navigation

    sealed class MainNavigationScreen(
        route: String,
        val iconSelectedId: Int,
        val iconUnselectedId: Int
    ): Screen(route) {
        data object CameraScreen: MainNavigationScreen(
            route = "camera",
            iconSelectedId = R.drawable.photo_camera_filled,
            iconUnselectedId = R.drawable.photo_camera
        )

        data object AnalyticsScreen: MainNavigationScreen(
            route = "analytics",
            iconSelectedId = R.drawable.analytics_filled,
            iconUnselectedId = R.drawable.analytics
        )

        data object SettingsScreen: MainNavigationScreen(
            route = "settings",
            iconSelectedId = R.drawable.settings_filled,
            iconUnselectedId = R.drawable.settings
        )
    }
}

val mainNavigationScreens: List<Screen.MainNavigationScreen> =
    listOf(
        Screen.MainNavigationScreen.CameraScreen,
        Screen.MainNavigationScreen.AnalyticsScreen,
        Screen.MainNavigationScreen.SettingsScreen
    )