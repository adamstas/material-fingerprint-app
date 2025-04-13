package cz.cas.utia.materialfingerprintapp.core.launch.presentation

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.setting.domain.SettingsRepository
import cz.cas.utia.materialfingerprintapp.features.setting.presentation.permission.requiredPermissions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LaunchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private val _state = MutableStateFlow<LaunchScreenState>(LaunchScreenState.Loading)
    val state: StateFlow<LaunchScreenState> = _state.asStateFlow()

    private fun initialize() {
        viewModelScope.launch {
            val permissionsGranted = checkPermissions()
            val tutorialCompleted = settingsRepository.getTutorialCompleted()

            _state.value = when {
                !permissionsGranted -> LaunchScreenState.ShowPermissionsScreen
                !tutorialCompleted -> LaunchScreenState.ShowTutorial
                else -> LaunchScreenState.LaunchMainContent
            }
        }
    }

    init {
        initialize()
    }

    private fun checkPermissions(): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun onEvent(event: LaunchEvent) {
        when (event) {
            LaunchEvent.TutorialCompleted -> tutorialCompleted()
            LaunchEvent.PermissionsGranted -> permissionsGranted()
        }
    }

    private fun tutorialCompleted() {
        initialize()
    }

    private fun permissionsGranted() {
        initialize()
    }
}