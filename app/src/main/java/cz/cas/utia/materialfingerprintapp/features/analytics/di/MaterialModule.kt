package cz.cas.utia.materialfingerprintapp.features.analytics.di

import android.content.Context
import androidx.room.Room
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialDao
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialDatabase
import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.MaterialSummaryMapper
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//todo maybe move to core? and rename to MaterialDatabase module or something like that?
@Module
@InstallIn(SingletonComponent::class)
object MaterialModule {

    @Provides
    @Singleton
    fun provideMaterialDatabase(@ApplicationContext applicationContext: Context): MaterialDatabase {
        return Room.databaseBuilder(
            applicationContext,
            MaterialDatabase::class.java,
            "materials.db"
        ).build()
        //db.materialDao.insertMaterials(initialData()) //todo remove later - if needed to seed database, put it somewhere else in some init block or something
    }

    @Provides
    @Singleton
    fun provideMaterialDao(materialDatabase: MaterialDatabase): MaterialDao {
        return materialDatabase.materialDao
    }

    @Provides
    @Singleton
    fun provideMaterialSummaryMapper(imageStorageService: ImageStorageService): MaterialSummaryMapper {
        return MaterialSummaryMapper(imageStorageService)
    }
}