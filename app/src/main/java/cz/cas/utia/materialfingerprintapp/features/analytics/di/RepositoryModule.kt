package cz.cas.utia.materialfingerprintapp.features.analytics.di

import cz.cas.utia.materialfingerprintapp.features.analytics.data.material.RoomMaterialRepositoryImpl
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.LocalMaterialRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsProtoDataStore
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.MaterialCharacteristicsRepository
import cz.cas.utia.materialfingerprintapp.features.analytics.data.repository.RemoteMaterialRepository
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
        roomMaterialRepositoryImpl: RoomMaterialRepositoryImpl
    ): LocalMaterialRepository

//    @Binds
//    @Singleton
//    abstract fun bindRemoteMaterialRepository(
//        roomMaterialRepositoryImpl: RoomMaterialRepositoryImpl //todo change to remote repo, currently not working
//    ): RemoteMaterialRepository

    @Binds
    @Singleton
    abstract fun bindMaterialCharacteristicsRepository(
        materialCharacteristicsProtoDataStore: MaterialCharacteristicsProtoDataStore
    ): MaterialCharacteristicsRepository
}