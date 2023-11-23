package com.esightcorp.mobile.app.ui.extensions

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.esightcorp.mobile.app.ui.navigation.Navigation

//region NavGraphBuilder
fun NavGraphBuilder.composable(
    target: Navigation, content: @Composable (NavBackStackEntry) -> Unit
) = this.composable(target.path, content = content)
//endregion

//region NavController

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
