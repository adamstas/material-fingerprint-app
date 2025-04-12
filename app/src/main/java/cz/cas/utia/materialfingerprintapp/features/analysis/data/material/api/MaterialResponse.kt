package cz.cas.utia.materialfingerprintapp.features.analysis.data.material.api

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialImage
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary

data class MaterialResponse(
    val id: Long,
    val name: String,
    val category: MaterialCategory,

    val characteristics: MaterialCharacteristicsRequestResponse
) {
    fun toMaterialSummary(): MaterialSummary {
        return MaterialSummary(
            id = this.id,
            name = this.name,
            photoThumbnail = MaterialImage.UrlImage,
            category = this.category,
            characteristics = this.characteristics.toMaterialCharacteristics()
        )
    }

    fun toMaterial(): Material {
        return Material(
            serverId = if (this.id == -1L) null else this.id,
            name = this.name,
            category = this.category,
            characteristics = this.characteristics.toMaterialCharacteristics()
        )
    }
}