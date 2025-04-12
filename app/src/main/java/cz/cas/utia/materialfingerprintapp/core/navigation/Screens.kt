package cz.cas.utia.materialfingerprintapp.core.navigation

import cz.cas.utia.materialfingerprintapp.R
import kotlinx.serialization.Serializable

enum class ScreenGroup {
    CAPTURING,
    ANALYSIS,
    SETTINGS
}

val mainScreens = listOf(
    Screen.MainScreen.Capturing,
    Screen.MainScreen.Analysis,
    Screen.MainScreen.Settings
    )

sealed class Screen {

    abstract val group: ScreenGroup

    // for proper bottom bar icons highlighting
    companion object {
        fun getGroupByClassName(className: String?): ScreenGroup? {
            return when (className) {
                Capturing::class.qualifiedName -> ScreenGroup.CAPTURING
                PhotosSummary::class.qualifiedName -> ScreenGroup.CAPTURING

                AnalysisHome::class.qualifiedName -> ScreenGroup.ANALYSIS
                BrowseLocalMaterials::class.qualifiedName -> ScreenGroup.ANALYSIS
                BrowseRemoteMaterials::class.qualifiedName -> ScreenGroup.ANALYSIS
                BrowseSimilarLocalMaterials::class.qualifiedName -> ScreenGroup.ANALYSIS
                BrowseSimilarRemoteMaterials::class.qualifiedName -> ScreenGroup.ANALYSIS
                PolarPlotVisualisation::class.qualifiedName -> ScreenGroup.ANALYSIS
                ApplyFilter::class.qualifiedName -> ScreenGroup.ANALYSIS

                Settings::class.qualifiedName -> ScreenGroup.SETTINGS
                Tutorial::class.qualifiedName -> ScreenGroup.SETTINGS

                else -> null
            }
        }
    }

    @Serializable
    sealed class MainScreen<T>(val label: String, val iconSelectedId: Int, val iconUnselectedId: Int, val route: T): Screen() {
        @Serializable
        data object Capturing: MainScreen<Screen.Capturing>(
            label = "Capturing",
            iconSelectedId = R.drawable.photo_camera_filled,
            iconUnselectedId = R.drawable.photo_camera,
            route = Screen.Capturing
        ) {
            override val group = ScreenGroup.CAPTURING
        }

        @Serializable
        data object Analysis: MainScreen<AnalysisHome>(
            label = "Analysis",
            iconSelectedId = R.drawable.analytics_filled,
            iconUnselectedId = R.drawable.analytics,
            route = AnalysisHome
        ) {
            override val group = ScreenGroup.ANALYSIS
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
    data object Capturing: Screen() {
        override val group = ScreenGroup.CAPTURING
    }

    @Serializable
    data object Settings: Screen() {
        override val group = ScreenGroup.SETTINGS
    }

    @Serializable
    data object AnalysisHome: Screen() {
        override val group = ScreenGroup.ANALYSIS
    }

    @Serializable
    data object PhotosSummary: Screen() {
        override val group = ScreenGroup.CAPTURING
    }

    @Serializable
    data object BrowseLocalMaterials: Screen() {
        override val group = ScreenGroup.ANALYSIS
    }

    @Serializable
    data object BrowseRemoteMaterials: Screen() {
        override val group = ScreenGroup.ANALYSIS
    }

    @Serializable
    data class ApplyFilter(
        val loadCharacteristicsFromStorage: Boolean
    ): Screen() {
        override val group = ScreenGroup.ANALYSIS
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
        override val group = ScreenGroup.ANALYSIS
    }

    @Serializable
    data class BrowseSimilarLocalMaterials(val materialId: Long): Screen() {
        override val group = ScreenGroup.ANALYSIS
    }

    @Serializable
    data class BrowseSimilarRemoteMaterials(val materialId: Long): Screen() {
        override val group = ScreenGroup.ANALYSIS
    }

    @Serializable
    data object Tutorial: Screen() {
        override val group = ScreenGroup.SETTINGS
    }
}