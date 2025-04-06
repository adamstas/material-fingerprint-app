package cz.cas.utia.materialfingerprintapp.features.setting.data

import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.DefaultScreen

interface SettingsRepository {
    suspend fun saveSendDataToServerChoice(value: Boolean)
    suspend fun getSendDataToServerChoice(): Boolean

    suspend fun saveDefaultScreen(screen: DefaultScreen)
    suspend fun getDefaultScreen(): DefaultScreen
}