package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun LoadingScreenWithSpinner(
    modifier: Modifier = Modifier,
    loadingText: String = "Loading..."
) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout {
            val (text, spinner) = createRefs()

            Header2Text(
                text = loadingText,
                modifier = modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            CircularProgressIndicator(
                modifier = modifier
                    .size(75.dp)
                    .constrainAs(spinner) {
                        top.linkTo(text.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                color = Color.Yellow,
                strokeWidth = 15.dp
            )
        }
    }
}

@Preview
@Composable
fun LoadingScreenWithSpinnerPreview(){
    LoadingScreenWithSpinner()
}