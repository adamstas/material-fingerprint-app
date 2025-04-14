package cz.cas.utia.materialfingerprintapp.features.analysis.di

import cz.cas.utia.materialfingerprintapp.features.analysis.data.repository.RetrofitRemoteMaterialRepositoryImpl
import cz.cas.utia.materialfingerprintapp.features.analysis.data.repository.RoomLocalMaterialRepositoryImpl
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.data.repository.MaterialCharacteristicsProtoDataStore
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analysis.domain.repository.RemoteMaterialRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLocalMaterialRepository(
        roomMaterialRepositoryImpl: RoomLocalMaterialRepositoryImpl
    ): LocalMaterialRepository

    @Binds
    @Singleton
    abstract fun bindRemoteMaterialRepository(
        retrofitRemoteMaterialRepositoryImpl: RetrofitRemoteMaterialRepositoryImpl
    ): RemoteMaterialRepository

    @Binds
    @Singleton
    abstract fun bindMaterialCharacteristicsRepository(
        materialCharacteristicsProtoDataStore: MaterialCharacteristicsProtoDataStore
    ): MaterialCharacteristicsRepository
}