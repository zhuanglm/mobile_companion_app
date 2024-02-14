/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.home

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.PermissionScreen


@Composable
fun LocationServiceOffRoute(
    navController: NavController,
) = LocationServiceOffScreen(navController)

//region Private implementation

// TODO: using PermissionScreen as there is no string resource for this location service disabled use case
@Composable
private fun LocationServiceOffScreen(
    navController: NavController,
) = PermissionScreen(
    navController = navController,
    okLabelId = R.string.kRetryButtonTitle,
    descriptionId = R.string.kPermissionLocation,
    onCancelPressed = { it.popBackStack() },
    onOpenAppSettingPressed = { it.popBackStack() },
)

@Preview
@Composable
private fun LocationServiceOffScreenPreview() = MaterialTheme {
    LocationServiceOffScreen(navController = rememberNavController())
}
//endregion
