/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.extensions

import android.annotation.SuppressLint
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
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
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
    popUntil: Navigation?,
    popIncluded: Boolean,
): Boolean = navigate(
    target = target,
    popUntil = popUntil,
    popIncluded = popIncluded,
    param = null,
)

fun NavController.navigate(
    target: Navigation,
    popCurrent: Boolean,
) = navigate(target.path) {
    if (popCurrent) popBackStack()
}

fun NavController.navigate(
    target: Navigation,
    param: String? = null,
    popUntil: Navigation? = null,
    popIncluded: Boolean = true,
): Boolean {
    val isPopSuccess = popUntil?.let { popBackStack(it.path, popIncluded) } ?: popBackStack()

    val targetUri = when (param) {
        null -> target.path
        else -> "${target.path}/$param"
    }

    Log.i("Navigation", "Navigating to: $targetUri")

    navigate(targetUri) {
        launchSingleTop = true
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
@SuppressLint("RestrictedApi")
fun BackStackLogger(navController: NavController, tag: String? = null) {
    Log.w(
        tag, "Back-stack:\n${navController.currentBackStack.collectAsState().value.toStringList()}"
    )
}
