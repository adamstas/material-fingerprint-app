package cz.cas.utia.materialfingerprintapp.features.setting.data

import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.DefaultScreen

interface SettingsRepository {
    suspend fun saveStoreDataOnServerChoice(value: Boolean)
    suspend fun getStoreDataOnServerChoice(): Boolean

    suspend fun saveDefaultScreen(screen: DefaultScreen)
    suspend fun getDefaultScreen(): DefaultScreen

    suspend fun saveTutorialCompleted(completed: Boolean)
    suspend fun getTutorialCompleted(): Boolean
}