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
}