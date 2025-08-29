package cz.cas.utia.materialfingerprintapp.features.setting.data

import android.content.ContentResolver
import android.net.Uri
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.IMAGES_PATH
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.NON_SPECULAR_IMAGES_PATH
import cz.cas.utia.materialfingerprintapp.core.AppConfig.ImageStoring.SPECULAR_IMAGES_PATH
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialSummary
import cz.cas.utia.materialfingerprintapp.features.setting.domain.MaterialExportService
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import java.io.FileInputStream
import java.io.File
import java.io.OutputStreamWriter
import javax.inject.Inject

class MaterialExportServiceImpl @Inject constructor(
    private val contentResolver: ContentResolver,
    @ApplicationContext private val context: android.content.Context
) : MaterialExportService {

    override fun checkIfAnyImagesToExport(): Boolean {
        val specularDir = File(context.filesDir, "$IMAGES_PATH/$SPECULAR_IMAGES_PATH")
        val nonSpecularDir = File(context.filesDir, "$IMAGES_PATH/$NON_SPECULAR_IMAGES_PATH")

        val specularFiles = specularDir.listFiles().orEmpty()
        val nonSpecularFiles = nonSpecularDir.listFiles().orEmpty()

        return specularFiles.isNotEmpty() || nonSpecularFiles.isNotEmpty()
    }

    override suspend fun exportMaterialsAsCsv(uri: Uri, materials: List<MaterialSummary>) {
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

    override suspend fun exportAllLocalMaterialImagesAsZip(uri: Uri) {
        val specularDir = File(context.filesDir, "$IMAGES_PATH/$SPECULAR_IMAGES_PATH")
        val nonSpecularDir = File(context.filesDir, "$IMAGES_PATH/$NON_SPECULAR_IMAGES_PATH")

        contentResolver.openOutputStream(uri)?.use { outputStream ->
            ZipOutputStream(outputStream).use { zipStream ->
                addDirectoryToZip(zipStream, specularDir, "specular")
                addDirectoryToZip(zipStream, nonSpecularDir, "nonspecular")
            }
        }
    }

    private fun addDirectoryToZip(zipStream: ZipOutputStream, dir: File, zipFolderName: String) {
        dir.listFiles()?.forEach { file ->
            val entryName = "$zipFolderName/${file.name}" // e.g. "specular/1.png"
            FileInputStream(file).use { fis ->
                val entry = ZipEntry(entryName)
                zipStream.putNextEntry(entry) // prepare for writing next file to ZIP
                fis.copyTo(zipStream)
                zipStream.closeEntry()
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