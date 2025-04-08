package cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial

import cz.cas.utia.materialfingerprintapp.R

data class TutorialPage(
    val title: String,
    val description: String,
    val imageResId: Int
)

// todo posledni tutorialova screena:
// dokoncili jste tutorial, ted vas presmerujeme do nastaveni kde si nezapomente nastavit, zda chcete ci nechcete aby vase materialy byly ukladany na serveru.
// pokud se budete chtit k tutorialu kdykoli vratit, lze to provest taktez z nastaveni.
// at se vam aplikace libi

// todo zminit se tam o sablone asi? mozna na te posledni screeně tutorialové dat "nezapomente si vytisknout sablonu pro foceni materialu z TODO ODKAZ" ? anebo bez odkazu jen zminit?
// ale oan ta sablona v nejakem kroku bude zminena a bude i vidit kdyz fotim, jak to zezelenalo (to dat do tutorialu taky jako screenshot)

val tutorialPages: List<TutorialPage> = listOf(
// todo update to real data
    TutorialPage(
        "Welcome",
        "This is the first page of the tutorial.",
        R.drawable.ic_launcher_background
    ),
    TutorialPage(
        "Discover Features",
        "Here you can learn about features.",
        R.drawable.ic_launcher_foreground
    ),
    TutorialPage("Get Started", "Let's get started with the app.", R.drawable.latka4)
)