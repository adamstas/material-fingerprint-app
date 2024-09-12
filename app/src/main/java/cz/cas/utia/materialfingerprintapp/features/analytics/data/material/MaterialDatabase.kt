package cz.cas.utia.materialfingerprintapp.features.analytics.data.material

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Material::class],
    version = 1 //todo check how to migrate and maybe implement it here
)
abstract class MaterialDatabase: RoomDatabase() {

    abstract val materialDao: MaterialDao
}