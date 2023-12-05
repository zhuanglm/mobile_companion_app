package com.esightcorp.mobile.app.ui.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar

@Composable
fun BaseSurface(
    modifier: Modifier = Modifier,
    everythingElse: @Composable () -> Unit,
) {
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        everythingElse()
    }
}

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
 * @param bottomButton A composable function that renders the bottom button.
 * @param everythingElse A composable function that renders the contents of the screen between the `ESightTopAppBar` and the bottom button.
 *
 * @see ConstraintLayout
 * @see ESightTopAppBar
 * @sample BaseScreenPreview
 */
@Composable
fun BaseScreen(
    modifier: Modifier,
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    onBackButtonInvoked: () -> Unit,
    onSettingsButtonInvoked: () -> Unit,
    isBottomButtonNeeded: Boolean = true,
    bottomButton: @Composable () -> Unit,
    everythingElse: @Composable () -> Unit,
) {
    BaseSurface(modifier = modifier.systemBarsPadding()) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topAppBar, everything, btBtn) = createRefs()
            ESightTopAppBar(
                showBackButton = showBackButton,
                showSettingsButton = showSettingsButton,
                onBackButtonInvoked = onBackButtonInvoked,
                onSettingsButtonInvoked = onSettingsButtonInvoked,
                modifier = modifier.constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )
            Box(modifier = modifier
                .constrainAs(everything) {
                    top.linkTo(topAppBar.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(btBtn.top)
                    height = Dimension.fillToConstraints
                }
                .padding(25.dp, 0.dp, 25.dp, 0.dp)
                .fillMaxSize()
            ) {
                everythingElse()
            }
            if (isBottomButtonNeeded) {
                Box(modifier = modifier
                    .constrainAs(btBtn) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center) {
                    bottomButton()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseScreenPreview() {
    MaterialTheme {
        BaseScreen(
            modifier = Modifier,
            showBackButton = true,
            showSettingsButton = true,
            onBackButtonInvoked = {},
            onSettingsButtonInvoked = {},
            isBottomButtonNeeded = true,
            bottomButton = { Text("This is a bottom button") },
            everythingElse = { Text("This is the content of the screen") }
        )
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
    BaseSurface(modifier = modifier) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topAppBar, everything, btBtn) = createRefs()
            ESightTopAppBar(
                showBackButton = showBackButton,
                showSettingsButton = showSettingsButton,
                onBackButtonInvoked = onBackButtonInvoked,
                onSettingsButtonInvoked = onSettingsButtonInvoked,
                modifier = modifier.constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )
            Box(modifier = modifier
                .padding(25.dp, 30.dp, 25.dp, 0.dp)
                .constrainAs(everything) {
                    top.linkTo(topAppBar.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(btBtn.top)
                }
                .fillMaxSize()
            ) {
                everythingElse()
            }

            Box(
                modifier = modifier
                    .constrainAs(btBtn) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                bottomButton()
            }
        }
    }
}
