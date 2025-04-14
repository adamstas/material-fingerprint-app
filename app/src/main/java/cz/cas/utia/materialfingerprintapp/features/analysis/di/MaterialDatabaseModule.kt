package cz.cas.utia.materialfingerprintapp.features.analysis.di

import android.content.Context
import androidx.room.Room
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.MaterialDao
import cz.cas.utia.materialfingerprintapp.features.analysis.data.material.MaterialDatabase
import cz.cas.utia.materialfingerprintapp.features.analysis.presentation.MaterialSummaryMapper
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MaterialDatabaseModule {

    @Provides
    @Singleton
    fun provideMaterialDatabase(@ApplicationContext applicationContext: Context): MaterialDatabase {
        return Room.databaseBuilder(
            applicationContext,
            MaterialDatabase::class.java,
            "materials.db"
        ).build()
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