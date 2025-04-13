package cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository

import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material

//todo move those repositories to domain layer since they are just interfaces?
interface LocalMaterialRepository: MaterialRepository {
    suspend fun insertMaterial(material: Material): Long
}