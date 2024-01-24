/*
 * *LICENSE Copyright (C) 2009-2024 by Gentex Technology Canada. All Rights
 * Reserved.The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, Gentex Technology Canada, which intends to
 * preserve as trade secrets such software and information.
 */

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
