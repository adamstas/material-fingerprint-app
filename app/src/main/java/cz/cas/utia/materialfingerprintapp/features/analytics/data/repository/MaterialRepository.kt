package cz.cas.utia.materialfingerprintapp.features.analytics.data.repository

import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCharacteristics
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialSummary

interface MaterialRepository {
    suspend fun getAllMaterialsOrderedByName(): List<MaterialSummary>

    suspend fun getAllSimilarMaterialsOrderedByName(materialId: Long): List<MaterialSummary>
    suspend fun getAllSimilarMaterialsOrderedByName(materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String): List<MaterialSummary>

    suspend fun getSimilarMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String, materialId: Long): List<MaterialSummary>
    suspend fun getSimilarMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String, materialCharacteristics: MaterialCharacteristics): List<MaterialSummary>

    //todo tato metoda bude vubec potreba? nebo jen pridat characteristics do getMaterialsOrderedByName(..)?
    suspend fun getAllMaterialsSortedBySimilarity(characteristics: MaterialCharacteristics): List<MaterialSummary> //room material repository bude mit private servicku, ktera mu to spocita

    suspend fun getMaterialsCount(): Long
    suspend fun getMaterial(id: Long): Material //todo nebo to lze dat niz jen k tem repum, ktere to potrebuji?
}

//todo 4:
// a servicku pro pocitání podobného materiálu udělat tak, ze repositrář ji bude volat, nikoli přímo ti viewmodelové, co ji budou potřebovat, protože oni sami vědí, jaký repositář jim to má udělat, takže oni sami si ho zavolají a až ta implementace lokálního room repositáře bude mít u sebe tu srevicku, aby si mohla nechat ty materiály porovnat dle té podobnosti