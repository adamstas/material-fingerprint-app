package cz.cas.utia.materialfingerprintapp.core

import cz.cas.utia.materialfingerprintapp.R

object AppConfig {

    object Server {
        const val ip: String = "localhost" // todo pouzit jiny typ nez string? string asi staci..
    }

    object Colors {
        val primaryPlotColorId = R.color.matplotlib_orange
        val secondaryPlotColorId = R.color.matplotlib_blue
    }

    object PolarPlot {
        const val characteristicsMin = -2.75
        const val characteristicsMax = 2.75

        val axisLabels = listOf( // todo nechat tady nebo hodit do nejakeho Core UI pro analytics?
            "Brightness",
            "Color vibrancy",
            "Hardness",
            "Checkered pattern",
            "Movement effect",
            "Multicolored",
            "Naturalness",
            "Pattern complexity",
            "Scale of pattern",
            "Shininess",
            "Sparkle",
            "Striped pattern",
            "Surface roughness",
            "Thickness",
            "Value",
            "Warmth"
        )
    }
}