package com.esightcorp.mobile.app.ui.navigation

import androidx.navigation.NavController

/**
 * General type name for an event/action that involves navigation update
 */
typealias OnNavigationCallback = (NavController) -> Unit

/**
 * General type name for a user action event
 */
typealias OnActionCallback = () -> Unit
