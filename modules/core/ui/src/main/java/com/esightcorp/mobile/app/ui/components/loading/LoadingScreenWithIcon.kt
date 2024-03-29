/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.ui.components.loading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.text.BoldSubheader


@Composable
fun LoadingScreenWithIcon(
    modifier: Modifier = Modifier,
    loadingText: String = "Loading..."
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout (modifier = modifier.fillMaxSize().padding(25.dp, 0.dp)){
            val (topAppBar, text, icon) = createRefs()
            ESightTopAppBar(
                showBackButton = false,
                showSettingsButton = false ,
                onBackButtonInvoked = { /*Unused*/ },
                onSettingsButtonInvoked = { /*Unused*/ },
                modifier = modifier.constrainAs(topAppBar){
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            BoldSubheader(
                text = loadingText,
                textAlign = TextAlign.Center,
                modifier = modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Icon(
                Icons.Rounded.Check,
                contentDescription = stringResource(id = R.string.kAccessibilityIconCheck),
                modifier = modifier
                    .size(75.dp)
                    .constrainAs(icon) {
                        top.linkTo(text.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                tint = MaterialTheme.colors.primary
            )
        }
    }
}

@Preview
@Composable
fun LoadingScreenWithIconPreview() {
    LoadingScreenWithIcon()
    
}