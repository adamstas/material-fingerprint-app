package cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial

sealed interface TutorialEvent {
    data object CompleteTutorial: TutorialEvent
}

sealed interface TutorialNavigationEvent {
    data object ToTutorialCompletedDestination: TutorialNavigationEvent
}