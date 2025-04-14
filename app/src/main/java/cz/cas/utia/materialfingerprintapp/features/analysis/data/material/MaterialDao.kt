package cz.cas.utia.materialfingerprintapp.features.analysis.data.material

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.MaterialCategory

@Dao
interface MaterialDao {
    @Insert
    suspend fun insertMaterial(material: Material): Long

    @Delete
    suspend fun deleteMaterial(material: Material) // currently unused

    @Query("SELECT * FROM material " +
            "WHERE category in (:categories) " +
            "AND name LIKE '%' || :searchText || '%'" +
            "ORDER BY LOWER(name) ASC")
    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String): List<Material>

    @Query("SELECT * FROM material ORDER BY name ASC")
    suspend fun getAllMaterialsOrderedByName(): List<Material>

    @Query("SELECT * FROM material WHERE id = :id")
    suspend fun getMaterial(id: Long): Material
}