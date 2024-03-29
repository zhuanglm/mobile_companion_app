/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

/**
 * Represents the base structure of a screen. It contains an `ESightTopAppBar` at the top,
 * a composable `everythingElse` to be placed below the top app bar, and an optional bottom button.
 * This structure is contained within a `BaseSurface` and a `ConstraintLayout`.
 *
 * @param modifier A [Modifier] applied to the base of the screen for layout and styling.
 * @param showBackButton If `true`, a back button is displayed in the `ESightTopAppBar`.
 * @param showSettingsButton If `true`, a settings button is displayed in the `ESightTopAppBar`.
 * @param onBackButtonInvoked A lambda function that will be triggered when the back button is clicked.
 * @param onSettingsButtonInvoked A lambda function that will be triggered when the settings button is clicked.
 * @param isBottomButtonNeeded If `true`, a bottom button is rendered at the bottom of the screen.
 * @param isContentCentered If `true`, the whole content (`everythingElse`) will be centered of the screen.
 * @param bottomButton A composable function that renders the bottom button.
 * @param bottomAlignedContent A composable function that stick on the bottom of content
 * @param everythingElse A composable function that renders the contents of the screen between the `ESightTopAppBar` and the bottom button.
 *
 * @see ConstraintLayout
 * @see ESightTopAppBar
 * @see Centered
 * @sample BaseScreenPreview
 */
@Composable
fun BaseScreen(
    modifier: Modifier,
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    onBackButtonInvoked: OnActionCallback? = null,
    onSettingsButtonInvoked: OnActionCallback? = null,
    isBottomButtonNeeded: Boolean = true,
    isContentCentered: Boolean = false,
    bottomButton: @Composable () -> Unit,
    bottomAlignedContent: @Composable (() -> Unit)? = null,
    everythingElse: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),

        backgroundColor = MaterialTheme.colors.surface,

        topBar = {
            ESightTopAppBar(
                showBackButton = showBackButton,
                showSettingsButton = showSettingsButton,
                onBackButtonInvoked = onBackButtonInvoked,
                onSettingsButtonInvoked = onSettingsButtonInvoked,
                modifier
            )
        },
        bottomBar = {
            if (isBottomButtonNeeded) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    bottomButton()
                }
            }

        },
    ) { innerPadding ->
        var contentModifier = Modifier
            .padding(25.dp, 0.dp, 25.dp, innerPadding.calculateBottomPadding())
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        if (isContentCentered)
            contentModifier = contentModifier.height(IntrinsicSize.Max)

        Column(modifier = contentModifier) {
            everythingElse()

            bottomAlignedContent?.let {
                // Spacer to fill up the available space
                Spacer(modifier = Modifier.weight(1f))
                it.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    MaterialTheme {
        BaseScreen(modifier = Modifier,
            showBackButton = true,
            showSettingsButton = true,
            onBackButtonInvoked = {},
            onSettingsButtonInvoked = {},
            isBottomButtonNeeded = true,
            bottomButton = { Text("This is a bottom button") },
            everythingElse = { Text("This is the content of the screen") })
    }
}

@Preview(showBackground = true)
@Composable
fun HomeBaseScreenPreview() {
    MaterialTheme {
        HomeBaseScreen(modifier = Modifier,
            showBackButton = true,
            showSettingsButton = true,
            onBackButtonInvoked = {},
            onSettingsButtonInvoked = {},
            bottomButton = { Text("This is a bottom button") },
            everythingElse = { Text("This is the content of the screen") })
    }
}

@Composable
fun HomeBaseScreen(
    modifier: Modifier,
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    onBackButtonInvoked: () -> Unit,
    onSettingsButtonInvoked: () -> Unit,
    bottomButton: @Composable () -> Unit,
    everythingElse: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .systemBarsPadding(),

        backgroundColor = MaterialTheme.colors.surface,

        topBar = {
            ESightTopAppBar(
                showBackButton = showBackButton,
                showSettingsButton = showSettingsButton,
                onBackButtonInvoked = onBackButtonInvoked,
                onSettingsButtonInvoked = onSettingsButtonInvoked,
                modifier
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                bottomButton()
            }

        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(25.dp, 0.dp, 25.dp, innerPadding.calculateBottomPadding())
                .fillMaxSize()
        ) {
            everythingElse()

        }
    }
}
