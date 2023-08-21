package com.esightcorp.mobile.app.eshare

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectionStoppedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.buttons.bottomButtons.CantFindDeviceButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon


@Composable
fun EshareConnectionStoppedRoute(
    navController: NavController,
    vm: EshareConnectionStoppedViewModel = hiltViewModel()
) {


    EshareConnectionStoppedScreen(
        navController = navController,
        onReturnButtonClicked = vm::navigateToNoDevicesConnectedScreen
    )
}

@Composable
fun EshareConnectionStoppedScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onReturnButtonClicked: (navController: NavController) -> Unit,
) {

    // Retrieve margin values from resources
    val headerTopMargin = dimensionResource(id = R.dimen.bt_disabled_header_top_margin)
    val bodyTopMargin = dimensionResource(id = R.dimen.bt_disabled_body_top_margin)


    //TODO: Make this into a reusable component
    BaseScreen(modifier = modifier,
        showBackButton = false,
        showSettingsButton = false,
        onBackButtonInvoked = { Unit },
        onSettingsButtonInvoked = { /*Unused*/ },
        bottomButton = {Unit}) {

        ConstraintLayout(modifier = modifier.fillMaxWidth()) {
            val (icon, header, subheader, button) = createRefs()
            // Set up the big Bluetooth icon
            BigIcon(
                painter = painterResource(id = R.drawable.link_off),
                contentDescription = "Disconnected chain link",
                modifier = modifier.constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })

            // Set up the header text
            Header1Text(
                text = stringResource(id = R.string.eshare_stopped_header),
                modifier = modifier.constrainAs(header) {
                    top.linkTo(icon.bottom, margin = headerTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            // Set up the body text
            Subheader(
                text = stringResource(id = R.string.eshare_stopped_subheader),
                modifier = modifier
                    .padding(
                        dimensionResource(id = R.dimen.bt_disabled_horizontal_padding),
                        dimensionResource(
                            id = R.dimen.zero
                        )
                    )
                    .constrainAs(subheader) {
                        top.linkTo(header.bottom, margin = bodyTopMargin)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                textAlign = TextAlign.Center

            )

            TextRectangularButton(onClick = { onReturnButtonClicked(navController) }, modifier = modifier.constrainAs(button){
                top.linkTo(subheader.bottom, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

            }, text = stringResource(id = R.string.eshare_stopped_button))


        }



    }
}
