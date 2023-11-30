package com.esightcorp.mobile.app.ui.extensions

import android.util.Log
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.esightcorp.mobile.app.ui.components.toStringList
import com.esightcorp.mobile.app.ui.navigation.Navigation

//region NavGraphBuilder
fun NavGraphBuilder.composable(
    target: Navigation,
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
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
    var isPopSuccess = popUntil?.let { popBackStack(it.path, popIncluded) } ?: popBackStack()

    // Check if the target is in the stack
    try {
        getBackStackEntry(target.path)

        // the target is in the stack, just pop until it
        isPopSuccess = popBackStack(target.path, false)
        Log.i("NavController", "Pop target: ${target.path}, success: $isPopSuccess")

    } catch (_: IllegalArgumentException) {
        // Good, not in the stack
    } finally {
        navigate(target.path) {
            launchSingleTop = true
        }
    }

    return isPopSuccess
}

//endregion

/**
 * Debug utility to print current backstack list
 *
 * @param navController The current navigation controller
 * @param tag (Optional) The tag
 */
@Composable
fun BackStackLogger(navController: NavController, tag: String? = null) {
    Log.w(
        tag,
        "Back-stack:\n${navController.currentBackStack.collectAsState().value.toStringList()}"
    )
}
