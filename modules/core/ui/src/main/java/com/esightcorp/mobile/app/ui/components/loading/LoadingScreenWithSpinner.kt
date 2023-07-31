package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.buttons.CancelButton

@Composable
fun LoadingScreenWithSpinner(
    modifier: Modifier = Modifier,
    loadingText: String = "Loading...",
    cancelButtonNeeded: Boolean = true,
    onCancelButtonClicked: () -> Unit = {},
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout {
            val (topAppBar, text, spinner, cancelButton) = createRefs()
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
            Subheader(
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
                color = MaterialTheme.colors.primary,
                strokeWidth = 10.dp
            )

            if(cancelButtonNeeded){
                CancelButton(onClick = onCancelButtonClicked, modifier = modifier.constrainAs(cancelButton){
                    start.linkTo(parent.start, margin= 25.dp)
                    bottom.linkTo(parent.bottom, margin = 25.dp)
                } )
            }
        }
    }
}

@Preview
@Composable
fun LoadingScreenWithSpinnerPreview(){
    LoadingScreenWithSpinner()
}