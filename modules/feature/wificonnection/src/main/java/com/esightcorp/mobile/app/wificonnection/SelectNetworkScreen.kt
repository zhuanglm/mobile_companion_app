package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.IconAndTextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.AdvancedSettingsButton


@Composable
fun SelectNetworkRoute(
    navController: NavController
) {
    SelectNetworkScreen(
        modifier = Modifier,
        navController = navController,
        onBackButtonClicked = { Unit },
        onNetworkButtonClicked = { Unit }
    )
}


//TODO: String resources
@Composable
internal fun SelectNetworkScreen(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit,
    onNetworkButtonClicked: () -> Unit,
    navController: NavController

) {
    val dummyNetworkList = listOf<String>(
        "Home Wi-Fi",
        "Office Wi-Fi",
        "It burns when IP",
        "FBI Mobile Unit",
        "VIRUS.EXE"
    )

    Surface(modifier = modifier.fillMaxSize(), color = Color.Black) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (topBar, header, networkContainer, advancedButton) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackButtonClicked,
                onSettingsButtonInvoked = { /*Unused*/ Unit },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Header1Text(
                text = "Select Wi-Fi network",
                modifier = modifier
                    .padding(25.dp, 0.dp)
                    .constrainAs(header) {
                        top.linkTo(topBar.bottom, margin = 50.dp)
                        start.linkTo(parent.start)
                    })
            LazyColumn(modifier = modifier
                .constrainAs(networkContainer) {
                    top.linkTo(header.bottom, margin = 35.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(advancedButton.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
                items(dummyNetworkList) { network ->
                    IconAndTextRectangularButton(
                        onClick = { Unit },
                        modifier = Modifier,
                        icon = ImageVector.vectorResource(id = com.esightcorp.mobile.app.ui.R.drawable.round_wifi_24),
                        text = network
                    )
                }
            }
            AdvancedSettingsButton(
                modifier = modifier.constrainAs(advancedButton){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onAdvancedSettingsClick = {Unit}
            )


        }
    }

}