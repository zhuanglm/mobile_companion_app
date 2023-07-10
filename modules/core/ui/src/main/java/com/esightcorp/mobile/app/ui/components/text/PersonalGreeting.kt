package com.esightcorp.mobile.app.ui.components.text

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.BodyText
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import java.time.LocalDateTime


/**
 * Displays a personal greeting message based on the current time of the day and
 * indicates the connection status of the user to an eSight.
 *
 * @param modifier a [Modifier] for the composable for styling and layout.
 * @param connected a Boolean flag indicating if the user is connected to an eSight.
 *
 * @sample PersonalGreetingPreview
 */
@Composable
fun PersonalGreeting(modifier: Modifier, connected: Boolean = false) {
    Box(modifier = modifier.fillMaxWidth().wrapContentHeight().semantics(mergeDescendants = true){}) {
        ConstraintLayout(
            modifier = modifier
        ) {
            val (greeting, connectionStatus) = createRefs()
            when (LocalDateTime.now().hour) {
                in 0..12 -> {
                    Header1Text(text = "Good Morning",
                        modifier = Modifier.constrainAs(greeting) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }, color = MaterialTheme.colors.onSurface
                    )
                }
                in 12..16 -> {
                    Header1Text(text = "Good Afternoon",
                        modifier = Modifier.constrainAs(greeting) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }, color = MaterialTheme.colors.onSurface
                    )
                }
                else -> {
                    Header1Text(text = "Good Evening",
                        modifier = Modifier.constrainAs(greeting) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }, color = MaterialTheme.colors.onSurface
                    )
                }
            }
            if (!connected) {
                BodyText(text = "You are not connected to an eSight",
                    modifier = Modifier.constrainAs(connectionStatus) {
                        top.linkTo(greeting.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }, color = MaterialTheme.colors.onSurface
                )
            } else {
                BodyText(
                    text = "You are connected to",
                    modifier = Modifier.constrainAs(connectionStatus) {
                        top.linkTo(greeting.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                    }, color = MaterialTheme.colors.onSurface
                )
            }


        }
    }

}

@Preview(showBackground = true)
@Composable
fun PersonalGreetingPreview() {
    MaterialTheme {
        PersonalGreeting(Modifier.padding(16.dp), false)
    }
}
