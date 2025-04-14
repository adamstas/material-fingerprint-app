package cz.cas.utia.materialfingerprintapp.features.analysis.domain

import javax.inject.Inject
import kotlin.math.pow
import kotlin.math.sqrt

class CalculateSimilarityUseCase @Inject constructor()
 {
    private fun getMaterialVectorFromMaterialSummary(materialSummary: MaterialSummary): DoubleArray {
        return doubleArrayOf(
            materialSummary.characteristics.brightness,
            materialSummary.characteristics.colorVibrancy,
            materialSummary.characteristics.hardness,
            materialSummary.characteristics.checkeredPattern,
            materialSummary.characteristics.movementEffect,
            materialSummary.characteristics.multicolored,
            materialSummary.characteristics.naturalness,
            materialSummary.characteristics.patternComplexity,
            materialSummary.characteristics.scaleOfPattern,
            materialSummary.characteristics.shininess,
            materialSummary.characteristics.sparkle,
            materialSummary.characteristics.stripedPattern,
            materialSummary.characteristics.surfaceRoughness,
            materialSummary.characteristics.thickness,
            materialSummary.characteristics.value,
            materialSummary.characteristics.warmth
        )
    }

    private fun getMaterialVectorFromCharacteristics(materialCharacteristics: MaterialCharacteristics): DoubleArray {
        return doubleArrayOf(
            materialCharacteristics.brightness,
            materialCharacteristics.colorVibrancy,
            materialCharacteristics.hardness,
            materialCharacteristics.checkeredPattern,
            materialCharacteristics.movementEffect,
            materialCharacteristics.multicolored,
            materialCharacteristics.naturalness,
            materialCharacteristics.patternComplexity,
            materialCharacteristics.scaleOfPattern,
            materialCharacteristics.shininess,
            materialCharacteristics.sparkle,
            materialCharacteristics.stripedPattern,
            materialCharacteristics.surfaceRoughness,
            materialCharacteristics.thickness,
            materialCharacteristics.value,
            materialCharacteristics.warmth
        )
    }
    private fun calculateSimilarityForTwoArrays(v1: DoubleArray, v2: DoubleArray, alpha: Double = 0.5): Double {
        require(v1.size == v2.size) { "Arrays must be the same length" }

        val size = v1.size
        val corr = pearsonCorrelation(v1, v2)
        val l1 = l1Norm(v1, v2)

        return alpha * corr + (1 - alpha) * (1 - (l1 / (2 * size)))
    }

    private fun pearsonCorrelation(v1: DoubleArray, v2: DoubleArray): Double {
        val mean1 = v1.average() // x_average
        val mean2 = v2.average() // y_average
        val deviations1 = v1.map { it - mean1 } // xi - x_average
        val deviations2 = v2.map { it - mean2 } // yi - y_average

        val numerator = deviations1.zip(deviations2) { d1, d2 -> d1 * d2 }.sum() // sum of (xi - x_average) * (yi - y_average)
        // sqrt from multiplication of sum of (xi - x_average) squared and sum of (yi - y_average) squared
        val denominator = sqrt(deviations1.sumOf { it.pow(2) } * deviations2.sumOf { it.pow(2) })

        return numerator / denominator
    }

    private fun l1Norm(v1: DoubleArray, v2: DoubleArray): Double {
        // sum of absolute values of the vector which represents distance between v1 and v2 (= subtraction of their coords)
        return v1.zip(v2) { a, b -> kotlin.math.abs(a - b) }.sum()
    }

    private fun calculateSimilarityForVector(targetVector: DoubleArray, materials: List<MaterialSummary>): List<MaterialSummary> {
        val similarities = materials.map { material ->
            val vector = getMaterialVectorFromMaterialSummary(material)
            val similarity = calculateSimilarityForTwoArrays(targetVector, vector)
            Pair(material, similarity)
        }

        return similarities.sortedByDescending { it.second }.map { it.first }
    }

    private fun filterMaterials(
        materials: List<MaterialSummary>,
        name: String?,
        categories: List<MaterialCategory>?
    ): List<MaterialSummary> {
        return materials.filter { material ->
            // default value is true so if name filter is null then matchesName is true
            val matchesName = name?.let { material.name.contains(it, ignoreCase = true) } ?: true
            val matchesCategory = categories?.contains(material.category) ?: true

            matchesName && matchesCategory
            }
    }

    fun calculateSimilarity(
        allMaterials: List<MaterialSummary>,
        targetMaterial: MaterialSummary,
        nameFilter: String? = null,
        categoryFilter: List<MaterialCategory>? = null)
    : List<MaterialSummary> {

        val targetVector = getMaterialVectorFromMaterialSummary(targetMaterial)
        val resultWithoutFilter = calculateSimilarityForVector(targetVector, allMaterials)

        return filterMaterials(resultWithoutFilter, nameFilter, categoryFilter)
    }

    fun calculateSimilarity(
        allMaterials: List<MaterialSummary>,
        targetMaterialCharacteristics: MaterialCharacteristics,
        nameFilter: String? = null,
        categoryFilter: List<MaterialCategory>? = null)
    : List<MaterialSummary> {

        val targetVector = getMaterialVectorFromCharacteristics(targetMaterialCharacteristics)
        val resultWithoutFilter = calculateSimilarityForVector(targetVector, allMaterials)

        return filterMaterials(resultWithoutFilter, nameFilter, categoryFilter)
    }
}