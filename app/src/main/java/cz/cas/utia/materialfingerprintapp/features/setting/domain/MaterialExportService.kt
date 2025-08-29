package cz.cas.utia.materialfingerprintapp.features.setting.domain

import android.net.Uri
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary

interface MaterialExportService {
    fun checkIfAnyImagesToExport(): Boolean
    suspend fun exportMaterialsAsCsv(uri: Uri, materials: List<MaterialSummary>)
    suspend fun exportAllLocalMaterialImagesAsZip(uri: Uri)
}