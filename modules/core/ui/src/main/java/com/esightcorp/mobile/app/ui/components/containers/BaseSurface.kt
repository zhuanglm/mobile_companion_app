package com.esightcorp.mobile.app.ui.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar

@Composable
fun BaseSurface(
    modifier: Modifier = Modifier, everythingElse: @Composable () -> Unit
) {
    Surface(modifier = modifier, color = MaterialTheme.colors.surface) {
        everythingElse()
    }
}

@Composable
fun BaseScreen(
    modifier: Modifier,
    showBackButton: Boolean,
    showSettingsButton: Boolean,
    onBackButtonInvoked: () -> Unit ,
    onSettingsButtonInvoked: () -> Unit ,
    everythingElse: @Composable () -> Unit,
    bottomButton: @Composable () -> Unit,

    ) {
    BaseSurface(modifier = modifier) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topAppBar, everything, bottomButton) = createRefs()
            ESightTopAppBar(showBackButton = showBackButton,
                showSettingsButton = showSettingsButton,
                onBackButtonInvoked = onBackButtonInvoked,
                onSettingsButtonInvoked = onSettingsButtonInvoked,
                modifier = modifier.constrainAs(topAppBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            LazyColumn(modifier = modifier
                .constrainAs(everything) {
                    top.linkTo(topAppBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(25.dp, 30.dp, 25.dp, 0.dp)) {
                item {
                    everythingElse()
                }
            }
            Box(modifier = modifier.constrainAs(bottomButton) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.fillMaxWidth(),
            contentAlignment = Alignment.Center) {
                bottomButton()
            }
        }
    }
}

