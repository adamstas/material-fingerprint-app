package cz.cas.utia.materialfingerprintapp.features.analysis.data.repository

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCharacteristics

interface MaterialCharacteristicsRepository {
    suspend fun saveMaterialCharacteristics(materialCharacteristics: MaterialCharacteristics, slot: MaterialCharacteristicsStorageSlot)
    suspend fun loadMaterialCharacteristics(slot: MaterialCharacteristicsStorageSlot): MaterialCharacteristics
}

enum class MaterialCharacteristicsStorageSlot {
    REMOTE_FIRST,
    REMOTE_SECOND,
    APPLY_FILTER_SCREEN
}