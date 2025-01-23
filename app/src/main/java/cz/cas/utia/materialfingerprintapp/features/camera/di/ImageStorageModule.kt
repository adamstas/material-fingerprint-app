package cz.cas.utia.materialfingerprintapp.features.camera.di

import cz.cas.utia.materialfingerprintapp.features.camera.data.image.ImageStorageServiceImpl
import cz.cas.utia.materialfingerprintapp.features.camera.domain.image.ImageStorageService
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ImageStorageModule {

    @Binds
    @Singleton
    abstract fun bindImageStorageService(
        imageStorageServiceImpl: ImageStorageServiceImpl
    ): ImageStorageService
}