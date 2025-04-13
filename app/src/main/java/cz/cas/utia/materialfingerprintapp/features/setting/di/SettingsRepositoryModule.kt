package cz.cas.utia.materialfingerprintapp.features.setting.di

import cz.cas.utia.materialfingerprintapp.features.setting.data.SettingsDataStore
import cz.cas.utia.materialfingerprintapp.features.setting.domain.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        settingsDataStore: SettingsDataStore
    ): SettingsRepository
}