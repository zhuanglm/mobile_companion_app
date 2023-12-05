package com.esightcorp.mobile.app.ui.components.icons

import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.esightcorp.mobile.app.ui.R
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
fun BigIcon(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    @DrawableRes drawableId: Int? = null,
    contentDescription: String? = null,
) {
    Surface(
        modifier = modifier.size(120.dp),
        shape = CircleShape,
        color = MaterialTheme.colors.primary
    ) {
        val iconPainter = painter ?: drawableId?.let {
            rememberDrawablePainter(AppCompatResources.getDrawable(LocalContext.current, it))
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            iconPainter?.let {
                Icon(
                    painter = it,
                    contentDescription = contentDescription,
                    modifier = modifier.size(75.dp),
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun BigIconPreview() {
    BigIcon(
        painter = painterResource(R.drawable.baseline_bluetooth_24),
        contentDescription = "Bluetooth Icon",
        modifier = Modifier
    )
}

@Preview
@Composable
fun BigIconPreview2() =
    BigIcon(contentDescription = "", drawableId = R.drawable.baseline_bluetooth_24)
