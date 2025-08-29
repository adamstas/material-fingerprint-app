package cz.cas.utia.materialfingerprintapp.core

import cz.cas.utia.materialfingerprintapp.R

object AppConfig {

    object Tutorial {
        const val PHOTO_CAPTURING_TEMPLATE_LINK = "https://raw.githubusercontent.com/adamstas/material-fingerprint-app/refs/heads/master/template/capturing_template.svg"
    }

    object MaterialExporting {
        const val FILE_BASENAME_CSV = "exported_materials"
        const val FILE_BASENAME_ZIP = "exported_material_images"
    }

    object ImageStoring {
        const val IMAGE_SUFFIX = ".png"
        const val SLOT1_IMAGE_NAME = "1"
        const val SLOT2_IMAGE_NAME = "2"

        const val SLOT1_IMAGE_NAME_WITH_SUFFIX = "$SLOT1_IMAGE_NAME$IMAGE_SUFFIX"
        const val SLOT2_IMAGE_NAME_WITH_SUFFIX = "$SLOT2_IMAGE_NAME$IMAGE_SUFFIX"

        const val IMAGES_PATH = "images"
        const val SPECULAR_IMAGES_PATH = "/specular"
        const val NON_SPECULAR_IMAGES_PATH = "/nonspecular"
        const val SLOT_IMAGES_PATH = "/slot"
    }

    object Server {
        const val DEFAULT_URL: String = "http://stimuly.utia.cas.cz:8000"
        //const val DEFAULT_URL: String = "http://127.0.0.1:8000"
        const val MATERIALS_URL: String = "$DEFAULT_URL/materials/"
        const val GET_MATERIAL_SPECULAR_IMAGE_URL_APPEND: String = "/image/specular"
        const val GET_MATERIAL_NON_SPECULAR_IMAGE_URL_APPEND: String = "/image/non_specular"
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