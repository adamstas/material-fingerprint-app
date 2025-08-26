package cz.cas.utia.materialfingerprintapp.features.setting.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cz.cas.utia.materialfingerprintapp.core.AppConfig
import cz.cas.utia.materialfingerprintapp.features.setting.domain.SettingsRepository
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.settings.DefaultScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>,
): SettingsRepository {

    // for API interceptor to read current URL without needing to call suspend function that accesses the data store
    private val _serverUrl = MutableStateFlow(DefaultValues.DEFAULT_SERVER_URL)

    init {
        // when startup store URL from datastore to state
        CoroutineScope(Dispatchers.IO).launch {
            _serverUrl.value = getServerUrl()
        }
    }

    private object DefaultValues {
        const val STORE_DATA_ON_SERVER_CHOICE = false
        val DEFAULT_SCREEN = DefaultScreen.SETTINGS
        const val TUTORIAL_COMPLETED = false
        const val DEFAULT_SERVER_URL = AppConfig.Server.DEFAULT_URL
    }

    private object PreferencesKeys {
        val STORE_DATA_ON_SERVER_KEY = booleanPreferencesKey("store_data_on_server_choice")
        val DEFAULT_SCREEN_KEY = stringPreferencesKey("default_screen")
        val TUTORIAL_COMPLETED = booleanPreferencesKey("tutorial_completed")
        val SERVER_URL_KEY = stringPreferencesKey("server_url")
    }

    private fun stringToDefaultScreen(text: String): DefaultScreen {
        return DefaultScreen.valueOf(text.uppercase())
    }

    override suspend fun saveStoreDataOnServerChoice(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.STORE_DATA_ON_SERVER_KEY] = value
        }
    }

    override suspend fun getStoreDataOnServerChoice(): Boolean {
        val flow = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.STORE_DATA_ON_SERVER_KEY] ?: DefaultValues.STORE_DATA_ON_SERVER_CHOICE
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

    override suspend fun saveServerUrl(url: String) {
        dataStore.edit { preferences ->
                preferences[PreferencesKeys.SERVER_URL_KEY] = url
        }
        // update also the URL in state
        _serverUrl.value = url
    }

    override suspend fun getServerUrl(): String {
        val flow = dataStore.data.map { preferences ->
            preferences[PreferencesKeys.SERVER_URL_KEY] ?: DefaultValues.DEFAULT_SERVER_URL
        }
        return flow.first()
    }

    override fun getServerUrlSync(): String = _serverUrl.value

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