package cz.cas.utia.materialfingerprintapp.features.analysis.data.material

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.Material

@Database(
    entities = [Material::class],
    version = 1
)
abstract class MaterialDatabase: RoomDatabase() {

    abstract val materialDao: MaterialDao
}