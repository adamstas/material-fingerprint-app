package cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.cas.utia.materialfingerprintapp.features.setting.data.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TutorialViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _navigationEvents = MutableSharedFlow<TutorialNavigationEvent>()
    val navigationEvents = _navigationEvents.asSharedFlow()

    fun onEvent(event: TutorialEvent) {
        when(event) {
            TutorialEvent.CompleteTutorial -> completeTutorial()
        }
    }

    private fun completeTutorial() {
        viewModelScope.launch {
            settingsRepository.saveTutorialCompleted(true)
            _navigationEvents.emit(TutorialNavigationEvent.ToTutorialCompletedDestination)
        }
    }
}