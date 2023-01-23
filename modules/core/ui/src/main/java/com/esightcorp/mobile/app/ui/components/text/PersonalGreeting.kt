package com.esightcorp.mobile.app.ui.components.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Header2Text
import java.time.LocalDateTime

@OptIn(ExperimentalUnitApi::class)
@Composable
fun PersonalGreeting(modifier: Modifier){
    Box(modifier = modifier.fillMaxWidth()){
        ConstraintLayout(
            modifier =
            Modifier
                .fillMaxWidth(0.8f)
                .wrapContentHeight()) {
            val (greeting, connectionStatus) = createRefs()
            when(LocalDateTime.now().hour){
                in 0..12 -> {
                    Header1Text(text = "Good Morning",
                        modifier = Modifier.constrainAs(greeting){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }
                in 12..16 -> {
                    Header1Text(text = "Good Afternoon",
                        modifier = Modifier.constrainAs(greeting){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }
                else -> {
                    Header1Text(text = "Good Evening",
                        modifier = Modifier.constrainAs(greeting){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }
            }
            Header2Text(text = "You are not connected to an eSight",
                modifier = Modifier.constrainAs(connectionStatus){
                    top.linkTo(greeting.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                })

        }
    }

}
