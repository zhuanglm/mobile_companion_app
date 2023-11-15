package com.esightcorp.mobile.app.ui.navigation

import androidx.navigation.NavController

//region Route definition

abstract class Navigation(open val path: String)

sealed class SettingsNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : SettingsNavigation("settings")
    object EntranceRoute : SettingsNavigation("settings_entrance")
    object DisconnectRoute : SettingsNavigation("settings_disconnect_device")
}

sealed class BtConnectionNavigation(override val path: String) : Navigation(path) {
    object IncomingRoute : BtConnectionNavigation("btconnection")

    //TODO: refactor all other route from BtConnectionScreens
}

//endregion

//region Extension utils

/**
 * Navigate to the `target` route.
 * If specifying, execute clean up (pop) the current stack until `popUntil` route with inclusive `popIncluded` or not **BEFORE** navigating to the `target`.
 *
 * @param target The target route to navigate to
 * @param popUntil (Optional) If specifying, pop current stack until this route
 * @param popIncluded (Optional) If specifying, include the `popUntil` route or not
 * @return Return value of the [NavController.popBackStack]
 */
fun NavController.navigate(
    target: Navigation,
    popUntil: Navigation? = null,
    popIncluded: Boolean = true,
): Boolean {
    val isPopSuccess = popUntil?.let { popBackStack(it.path, popIncluded) } ?: true
    navigate(target.path)

    return isPopSuccess
}

//endregion
