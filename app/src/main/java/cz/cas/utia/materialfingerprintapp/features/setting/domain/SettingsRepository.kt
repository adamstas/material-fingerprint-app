package cz.cas.utia.materialfingerprintapp.features.setting.domain

import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.DefaultScreen

interface SettingsRepository {
    suspend fun saveStoreDataOnServerChoice(value: Boolean)
    suspend fun getStoreDataOnServerChoice(): Boolean

    suspend fun saveDefaultScreen(screen: DefaultScreen)
    suspend fun getDefaultScreen(): DefaultScreen

    suspend fun saveServerUrl(url: String)
    suspend fun getServerUrl(): String

    // for getting the URL from state without "suspend" (for DynamicBaseUrlInterceptor)
    fun getServerUrlSync(): String

    suspend fun saveTutorialCompleted(completed: Boolean)
    suspend fun getTutorialCompleted(): Boolean
}