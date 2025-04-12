package cz.cas.utia.materialfingerprintapp.features.capturing.di

import cz.cas.utia.materialfingerprintapp.features.capturing.data.image.ImageStorageServiceImpl
import cz.cas.utia.materialfingerprintapp.features.capturing.domain.image.ImageStorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CameraModule {

    @Binds
    @Singleton
    abstract fun bindImageStorageService(
        imageStorageServiceImpl: ImageStorageServiceImpl
    ): ImageStorageService
}