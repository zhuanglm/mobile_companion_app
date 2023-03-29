package com.esightcorp.mobile.app.ui.components.loading

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.Subheader


@Composable
fun LoadingScreenWithIcon(
    modifier: Modifier = Modifier,
    loadingText: String = "Loading..."
) {
    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout {
            val (text, icon) = createRefs()

            Subheader(
                text = loadingText,
                modifier = modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Icon(
                Icons.Rounded.Check,
                contentDescription = "Connected to eSight",
                modifier = modifier.size(75.dp)
                    .constrainAs(icon){
                    top.linkTo(text.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                tint = MaterialTheme.colors.primary
            )
        }
    }
}