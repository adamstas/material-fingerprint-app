package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MaterialDao {
    //todo insert and delete functions may be marked "suspend" so they are pausable but will that be used? probably keep it..

    //todo maybe set UNIQUE to serverId attribute
    //todo data from server that are not photographed locally will be stored to the same database?
    @Insert
    suspend fun insertMaterial(material: Material)

    @Delete
    suspend fun deleteMaterial(material: Material) //todo will be used?

    @Query("SELECT * FROM material ORDER BY name ASC")
    fun getMaterialsOrderedByName(): Flow<List<Material>>
}