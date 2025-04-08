package cz.cas.utia.materialfingerprintapp.features.setting.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.DefaultScreen
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): SettingsRepository {

    private object DefaultValues {
        const val SEND_DATA_TO_SERVER_CHOICE = false
        val DEFAULT_SCREEN = DefaultScreen.SETTINGS
        const val TUTORIAL_COMPLETED = false
    }

    private object PreferencesKeys {
        val SEND_DATA_TO_SERVER_KEY = booleanPreferencesKey("send_data_to_server_choice")
        val DEFAULT_SCREEN_KEY = stringPreferencesKey("default_screen")
        val TUTORIAL_COMPLETED = booleanPreferencesKey("tutorial_completed")
    }

    private fun stringToDefaultScreen(text: String): DefaultScreen {
        return DefaultScreen.valueOf(text.uppercase())
    }

    override suspend fun saveSendDataToServerChoice(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SEND_DATA_TO_SERVER_KEY] = value
        }
    }

    override suspend fun getSendDataToServerChoice(): Boolean {
        val flow = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SEND_DATA_TO_SERVER_KEY] ?: DefaultValues.SEND_DATA_TO_SERVER_CHOICE
        }
        return flow.first()
    }

    override suspend fun saveDefaultScreen(screen: DefaultScreen) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.DEFAULT_SCREEN_KEY] = screen.name
        }
    }

    override suspend fun getDefaultScreen(): DefaultScreen {
        val flow = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.DEFAULT_SCREEN_KEY] ?: DefaultValues.DEFAULT_SCREEN.name
        }
        return stringToDefaultScreen(flow.first())
    }

    override suspend fun saveTutorialCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TUTORIAL_COMPLETED] = completed
        }
    }

    override suspend fun getTutorialCompleted(): Boolean {
        val flow = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.TUTORIAL_COMPLETED] ?: DefaultValues.TUTORIAL_COMPLETED
        }
        return flow.first()
    }
}