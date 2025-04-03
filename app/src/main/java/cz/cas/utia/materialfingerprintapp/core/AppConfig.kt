package cz.cas.utia.materialfingerprintapp.core

import cz.cas.utia.materialfingerprintapp.R

object AppConfig {

    object Server {
        const val URL: String = "http://127.0.0.1:8000"
        const val MATERIALS_URL: String = "$URL/materials/"
        const val GET_MATERIAL_IMAGE_URL_APPEND: String = "/image"
    }

    object Colors {
        val primaryPlotColorId = R.color.matplotlib_orange
        val secondaryPlotColorId = R.color.matplotlib_blue
    }

    object PolarPlot {
        const val CHARACTERISTICS_MIN = -2.75
        const val CHARACTERISTICS_MAX = 2.75

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