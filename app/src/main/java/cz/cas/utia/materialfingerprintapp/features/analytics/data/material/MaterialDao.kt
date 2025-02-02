package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.Material
import cz.cas.utia.materialfingerprintapp.features.analytics.domain.MaterialCategory

@Dao
interface MaterialDao {
    //todo insert and delete functions may be marked "suspend" so they are pausable but will that be used? probably keep it..

    //todo maybe set UNIQUE to serverId attribute
    //todo data from server that are not photographed locally will be stored to the same database?
    @Insert
    suspend fun insertMaterial(material: Material)

    @Insert
    suspend fun insertMaterials(materials: List<Material>) //now just for DB seeding

    @Delete
    suspend fun deleteMaterial(material: Material) //todo will be used?

    @Query("SELECT * FROM material " +
            "WHERE category in (:categories) " +
            "AND name LIKE '%' || :searchText || '%'" +
            "ORDER BY name ASC")
    suspend fun getMaterialsOrderedByName(categories: List<MaterialCategory>, searchText: String): List<Material>

    @Query("SELECT * FROM material ORDER BY name ASC")
    suspend fun getAllMaterialsOrderedByName(): List<Material>

    @Query("SELECT COUNT(*) FROM material")
    suspend fun getMaterialsCount(): Int //delete later, now used just for DB init

    @Query("DELETE FROM material") //just for testing, delete later
    suspend fun deleteAllMaterials()

    @Query("SELECT * FROM material WHERE id = :id") //todo nebo dat equals?
    suspend fun getMaterial(id: Int): Material
}