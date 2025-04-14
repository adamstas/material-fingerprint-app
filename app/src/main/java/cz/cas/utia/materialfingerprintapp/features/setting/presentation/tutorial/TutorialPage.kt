package cz.cas.utia.materialfingerprintapp.features.setting.presentation.tutorial

import cz.cas.utia.materialfingerprintapp.R

data class TutorialPage(
    val title: String,
    val description: String,
    val imageResId: Int,
    val hasLinkToTemplate: Boolean = false
)

val tutorialPages: List<TutorialPage> = listOf(
    TutorialPage(
        "Welcome to MatTag!",
        "This innovative application allows you to analyse material surfaces by capturing " + // todo upravit filip
                "their unique fingerprint patterns. You can then compare materials, find similar ones, " +
                "and apply custom filters to your or remote database of materials.",
        R.drawable.splash
    ),
    TutorialPage(
        "Printing the Template",
        "Before you begin, please print this scanning template." +
                " You can download it from link below.\n" +
                " This template is essential for properly scanning materials" +
                " during the capturing process and ensuring accurate analysis.", // todo filip upravi text
        R.drawable.ic_launcher_foreground, // todo obrazek sablony az bude hotovej
        hasLinkToTemplate = true
    ),
    TutorialPage(
        /**
         * 3 - capturing sekce - ze ma dve obrazovky a deje se tam capturing fotek. popsat obrazovku na
         * foceni, jake mit nasvetleni, jaky musi byt uhel apod.
         * Popsat tam i to, ze kdyz je ten uhel atd. spravny, tak ramecek zezelena a vy muzete poridit fotografii stisknutim tlacitka Capture.
         *  - dat fotku se sablonou, jak to je cervene a jak to je zelene (ale je potreba si svitit
         *  baterkou nebo druhym mobilem, takze tyhle fotky udelat, az filip pripravi popisky)
         *  - poslat filipovi jen screen jak ta obrazovka ted vypada
         */
        "Capturing Photos",
        "In the Capturing screen, you'll take photos of your material using the template. " + // todo text filip
                "When the phone is at the optimal angle, the frame will turn green, indicating you " +// todo rozdelit na vice obrazku a ukazat, jak to je zelene a cervene asi
                "can capture the image by pressing the Capture button. If the frame is red, " +
                "adjust your position until it turns green.\n" +
                "You'll need to take two photos of each material - one with side lighting and one with direct lighting from above.\n" +
                "After capturing your photos, tap the forward arrow to proceed to the Photos Summary screen.",
        R.drawable.photo_camera
    ), // todo camera obrazky
    TutorialPage(
        "Photos Summary",
        "Here, you'll enter information about the captured material and indicate which photo was " +
                "taken with side lighting and which with overhead lighting by tapping the sun or arrow icons." +
                " Once completed, select Analyse to send your data for processing.",
        R.drawable.analysis_filled // todo obrazek photos summary
    ),
    TutorialPage(
        "Polar Plot Visualisation",
        "On this screen, you'll see your analyzed material represented as a polar plot. Using the info icon in top " +
                "right corner of the screen you can toggle axis labels.\n" +
                "You have options to find similar materials or apply a custom filter to refine your search." +
                " We'll explore these features in detail later. For now, let's continue to the Analysis section.",
        R.drawable.photo_camera // todo obrazek polar plotu z daneho materialu, ktery budem fotit v tutorialu
    ),
    TutorialPage(
        "Analysis Home Screen",
        "When you navigate to the Analysis section using the bottom navigation bar, " +
                "you'll see options to browse local materials stored on your device or materials saved " +
                "on the remote server. You can also filter materials by adjusting values on the polar plot axes.\n" +
                "Now let’s try browsing local materials.",
        R.drawable.tutorial_analysis_home_screen_portrait
    ),
    TutorialPage(
        "Browsing Materials",
        "Let's demonstrate this using local materials (though it works the same way for remote materials). " +
                "Here you can browse through materials and search by name and category. You can display polar plots for one " +
                "or two selected materials, and find similar materials for a single selected item.\n" +
                "If you select just one material and tap Find Similar Materials, you'll be able " +
                "to choose if you want to discover similar materials in your device or remotely and then " +
                "you’ll see the materials.\n",
        R.drawable.tutorial_browse_local_materials_one_selected_portrait
    ),
    TutorialPage(
        "Finding Similar Materials",
        "Here you can browse materials similar to your selected material (either stored locally" +
                " or on the remote server). The interface works the same way as the Browse Materials " +
                "screen explained earlier." +
                "\n" +
                "When you select one or two materials, you can view its/their polar plot(s) by tapping on " +
                "Create polar plot button.",
        R.drawable.tutorial_browse_similar_materials_two_selected_portrait
    ),
    TutorialPage(
        "Polar Plot Visualization for Two Materials",
        "With two materials selected, you can toggle between a single plot with two curves" +
                " or two separate plots side by side using the segmented button. From here, you can find similar materials " +
                "(only for single selected material) or apply a filter.",
        R.drawable.tutorial_polar_plot_visualisation_two_materials_portrait
    ),
    TutorialPage(
        "Polar Plot Visualization for Single Material",
        "Now let’s say that on the Browse Materials screen we have checked just single material. " +
                "Then we can press Apply filter button which takes to Apply Filter screen.",
        R.drawable.tutorial_polar_plot_visualisation_one_material_portrait
    ),
    TutorialPage(
        "Apply Filter",
        "On the Apply Filter screen you can adjust values by dragging points on the plot. " +
                "The info icon toggles axis labels. If you make a mistake, use the Undo button to revert changes.\n" +
                "This custom polar plot serves as a filter for local or remote materials, " +
                "sorting them from most to least similar to your created pattern. Filtering can be done " +
                "using one of the two buttons on the screen.\n" +
                "You can also access this filter feature directly from the Analysis Home screen by " +
                "tapping \"Search for materials based on their fingerprint.\"",
        R.drawable.tutorial_apply_filter_touched_portrait
    ),
    TutorialPage(
        "Settings",
        "In the Settings section, you can choose whether your captured materials are " +
                "saved on the server. If this option is disabled, materials will still be analyzed on " +
                "the server and you'll see their polar plots, but they'll only be stored in your device, " +
                "not on the server.\n" +
                "You can also set your preferred starting screen and return to this tutorial at any time.",
        R.drawable.tutorial_settings_portrait
    ),
    TutorialPage(
        "Congratulations!",
        "You've completed the MatTag tutorial. Remember to disable storing your materials on the server in the Settings section " +
                "if you wish not to contribute to the shared database.\n" +
                "Don't forget to print the template, which can be found at link below.\n" +
                "We hope you enjoy using the application and discovering the unique fingerprints of different materials!",
        R.drawable.green_tick,
        hasLinkToTemplate = true
    ),

)