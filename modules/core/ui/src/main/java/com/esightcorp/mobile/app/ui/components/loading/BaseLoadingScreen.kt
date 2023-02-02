package com.esightcorp.mobile.app.ui.components.loading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.Header2Text

@Composable
fun BaseLoadingScreen(
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