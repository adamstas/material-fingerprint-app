package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary

interface MaterialRepository {
    suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary>
    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, nameSearch: String): List<MaterialSummary>

    suspend fun getAllSimilarMaterials(materialId: Long): List<MaterialSummary> // todo takhle to nedava smysl, musi to byt ordered by Similarity, jelikoz jinak by to nebyly similar materials + u ostatnich metod, kde nejsou similar materials, lze pridat dalsi parametr jako Order a udelat pro to nejaky Enum
    suspend fun getAllSimilarMaterials(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    suspend fun getSimilarMaterials(categories: List<MaterialCategory>, nameSearch: String, materialId: Long): List<MaterialSummary>
    suspend fun getSimilarMaterials(categories: List<MaterialCategory>, nameSearch: String, materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    suspend fun getMaterialsCount(): Long // todo toto asi odebrat, mozna nechat u lokalniho..ale asi pak ani tam ne
    suspend fun getMaterial(id: Long): Material //todo nebo to lze dat niz jen k tem repum, ktere to potrebuji?
}
//todo 4:
// a servicku pro pocitání podobného materiálu udělat tak, ze repositrář ji bude volat, nikoli přímo ti viewmodelové, co ji budou potřebovat, protože oni sami vědí, jaký repositář jim to má udělat, takže oni sami si ho zavolají a až ta implementace lokálního room repositáře bude mít u sebe tu srevicku, aby si mohla nechat ty materiály porovnat dle té podobnosti