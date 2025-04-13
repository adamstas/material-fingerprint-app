package cz.cas.utia.materialfingerprintapp.core

import cz.cas.utia.materialfingerprintapp.R

object AppConfig {

    object ImageStoring {
        const val IMAGE_SUFFIX = ".png"
        const val SLOT1_IMAGE_NAME = "slot1"
        const val SLOT2_IMAGE_NAME = "slot2"

        const val SLOT1_IMAGE_NAME_WITH_SUFFIX = "$SLOT1_IMAGE_NAME$IMAGE_SUFFIX"
        const val SLOT2_IMAGE_NAME_WITH_SUFFIX = "$SLOT2_IMAGE_NAME$IMAGE_SUFFIX"
    }

    object Server {
        const val URL: String = "http://127.0.0.1:8000"
        const val MATERIALS_URL: String = "$URL/materials/"
        const val GET_MATERIAL_IMAGE_URL_APPEND: String = "/image/specular"
    }

    object Colors {
        val primaryPlotColorId = R.color.matplotlib_orange
        val secondaryPlotColorId = R.color.matplotlib_blue
    }

    object PolarPlot {
        const val CHARACTERISTICS_MIN = -2.75
        const val CHARACTERISTICS_MAX = 2.75

        val axisLabels = listOf(
            "Checkered pattern",
            "Surface roughness",
            "Scale of pattern",
            "Multicolored",
            "Color vibrancy",
            "Brightness",
            "Naturalness",
            "Value",
            "Warmth",
            "Thickness",
            "Hardness",
            "Movement effect",
            "Shininess",
            "Sparkle",
            "Pattern complexity",
            "Striped pattern"
        )
    }
}