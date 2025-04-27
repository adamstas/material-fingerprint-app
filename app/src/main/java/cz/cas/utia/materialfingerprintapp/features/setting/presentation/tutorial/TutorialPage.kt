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
        "Discover the unique visual identity of materials through their perceptual fingerprints.\n" +
                "Capture material appearances, analyze key attributes, and explore or compare materials using intuitive visual patterns.\n" +
                "Filter and search your own or shared material databases — powered by human perception and smart visual encoding.",
        R.drawable.splash
    ),
    TutorialPage(
        "Printing the Template",
        "Print the scanning template from the link below. Cut out the red part with the cross.\n" +
                "This template is essential for properly scanning materials and ensuring accurate analysis.\n" +
                "Also, prepare a light source (e.g. flashlight).",
        R.drawable.tutorial_template,
        hasLinkToTemplate = true
    ),
    TutorialPage(
        "Capturing Photos I",
        "In the Capturing section, place the printed template on top of your flat material sample.\n" +
                "Ensure the material is flat and evenly lit. Hold your phone at about 45° elevation.\n" +
                "The frame turns green when properly aligned and red when adjustment is needed.\n" +
                "Take two photos in any order.\n" +
                "First photo: Side lighting (light source at 45° elevation, positioned to the side).",
        R.drawable.tutorial_capturing_first_photo_portrait
    ),
    TutorialPage(
        "Capturing Photos II",
        "Second photo: Opposite/top lighting, with the brightest specular reflection at the center of the image.\n" +
                "After capturing both photos, tap the forward arrow to review them.",
        R.drawable.tutorial_capturing_second_photo_portrait
    ),
    TutorialPage(
        "Photos Summary",
        "Enter information about the captured material and indicate which photo was taken " +
                "with side lighting and which with opposite/top lighting by tapping the sun or arrow icons.\n" +
                "Select Analyse to process your data.",
        R.drawable.tutorial_photos_summary_portrait
    ),
    TutorialPage(
        "Polar Plot Visualisation",
        "Your analyzed material appears as a polar plot. Toggle axis labels with the info icon in the top right corner.\n" +
                "Options include finding similar materials or applying a custom filter to refine your search.",
        R.drawable.tutorial_polar_plot_visualisation_from_analysis_portrait
    ),
    TutorialPage(
        "Analysis Home Screen",
        "The Analysis section offers options to browse materials stored locally or on the remote server.\n" +
                "You can filter materials by adjusting values on the polar plot axes.",
        R.drawable.tutorial_analysis_home_screen_portrait
    ),
    TutorialPage(
        "Browsing Materials",
        "Browse through materials and search by name and category (works the same for local and remote materials).\n" +
                "Display polar plots for one or two selected materials, and find similar materials for a single selected item.\n" +
                "With one material selected, tap Find Similar Materials to discover similar items on your device or remotely.",
        R.drawable.tutorial_browse_local_materials_one_selected_portrait
    ),
    TutorialPage(
        "Finding Similar Materials",
        "Browse materials similar to your selected material (stored locally or remotely).\n" +
                "The interface works the same as the Browse Materials screen.\n" +
                "When you select materials, view their polar plots by tapping Create polar plot.",
        R.drawable.tutorial_browse_similar_materials_two_selected_portrait
    ),
    TutorialPage(
        "Polar Plot Visualization for Two Materials",
        "With two materials selected, toggle between a single plot with two curves " +
                "or two separate plots using the segmented button.\n" +
                "Find similar materials (for single selected material only) or apply a filter.\n" +
                "Toggle axis labels using the info icon in top right corner.",
        R.drawable.tutorial_polar_plot_visualisation_two_materials_portrait
    ),
    TutorialPage(
        "Polar Plot Visualization for Single Material",
        "With a single material’s polar plot displayed, press Apply filter to access the Apply Filter screen.",
        R.drawable.tutorial_polar_plot_visualisation_one_material_portrait
    ),
    TutorialPage(
        "Apply Filter",
        "Adjust values by dragging points on the plot. Toggle axis labels with the info" +
                " icon. Use Undo to revert changes.\n" +
                "This custom polar plot serves as a filter for materials, sorting them from most to " +
                "least similar to your pattern.\n" +
                "Filter using the buttons at the bottom of the screen.\n" +
                "This feature is also accessible from the Analysis Home screen via Search for " +
                "materials based on their fingerprint.",
        R.drawable.tutorial_apply_filter_touched_portrait
    ),
    TutorialPage(
        "Settings",
        "Choose whether captured materials are saved on the server. If disabled, materials " +
                "will be analyzed but only stored on your device.\n" +
                "Set your preferred starting screen and access this tutorial at any time.",
        R.drawable.tutorial_settings_portrait
    ),
    TutorialPage(
        "Congratulations!",
        "You've completed the MatTag tutorial. Disable storing materials on the server in " +
                "Settings if you don't wish to contribute to the shared database.\n" +
                "Remember to print the template from the link below.\n" +
                "Enjoy discovering the unique fingerprints of different materials!",
        R.drawable.green_tick,
        hasLinkToTemplate = true
    )
)