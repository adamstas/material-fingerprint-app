package cz.cas.utia.materialfingerprintapp.features.setting.data

import android.content.ContentResolver
import android.net.Uri
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.setting.domain.MaterialExportService
import java.io.OutputStreamWriter
import javax.inject.Inject

class MaterialExportServiceImpl @Inject constructor(
    private val contentResolver: ContentResolver
) : MaterialExportService {

    override suspend fun exportMaterials(uri: Uri, materials: List<MaterialSummary>) {
        contentResolver.openOutputStream(uri)?.use { outputStream ->
            OutputStreamWriter(outputStream).use { writer ->
                // header row
                writer.write(buildHeaderRow())
                writer.write("\n")

                // data rows
                materials.forEach { material ->
                    writer.write(buildMaterialRow(material))
                    writer.write("\n")
                }
            }
        }
    }

    private fun buildHeaderRow(): String {
        val baseCols = listOf("name", "id", "category")
        val characteristicsCols = listOf(
            "checkeredPattern",
            "surfaceRoughness",
            "scaleOfPattern",
            "multicolored",
            "colorVibrancy",
            "brightness",
            "naturalness",
            "value",
            "warmth",
            "thickness",
            "hardness",
            "movementEffect",
            "shininess",
            "sparkle",
            "patternComplexity",
            "stripedPattern"
        )
        return (baseCols + characteristicsCols).joinToString(",")
    }

    private fun buildMaterialRow(material: MaterialSummary): String {
        val baseCols = listOf(
            material.name,
            material.id.toString(),
            material.category.name
        )
        val c = material.characteristics
        val characteristicsCols = listOf(
            c.checkeredPattern,
            c.surfaceRoughness,
            c.scaleOfPattern,
            c.multicolored,
            c.colorVibrancy,
            c.brightness,
            c.naturalness,
            c.value,
            c.warmth,
            c.thickness,
            c.hardness,
            c.movementEffect,
            c.shininess,
            c.sparkle,
            c.patternComplexity,
            c.stripedPattern
        ).map { it.toString() }

        return (baseCols + characteristicsCols).joinToString(",")
    }
}