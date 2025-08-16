package cz.cas.utia.materialfingerprintapp.features.setting.domain

import android.net.Uri
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary

interface MaterialExportService {
    suspend fun exportMaterials(uri: Uri, materials: List<MaterialSummary>)
}