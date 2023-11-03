package com.esightcorp.mobile.app.settings.navigation

sealed class SettingsScreens(val route: String) {
    object IncomingNavigatorRoute : SettingsScreens("settings")

    object SettingsEntranceRoute : SettingsScreens("settings_entrance")
    object SettingsHelpTutorialRoute : SettingsScreens("settings_help_tutorial")
    object SettingsHelpFeedbackRoute : SettingsScreens("settings_help_feedback")
    object SettingsAboutWebsiteRoute : SettingsScreens("settings_about_website")
    object SettingsAboutPolicyRoute : SettingsScreens("settings_about_policy")
}
