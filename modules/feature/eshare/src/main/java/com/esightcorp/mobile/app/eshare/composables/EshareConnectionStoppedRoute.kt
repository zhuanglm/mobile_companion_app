package com.esightcorp.mobile.app.eshare.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.eshare.viewmodels.EshareConnectionStoppedViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Subheader
import com.esightcorp.mobile.app.ui.components.TextRectangularButton
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon
import com.esightcorp.mobile.app.ui.navigation.OnNavigationCallback


@Composable
fun EshareConnectionStoppedRoute(
    navController: NavController,
    vm: EshareConnectionStoppedViewModel = hiltViewModel(),
) {
    EshareConnectionStoppedScreen(
        navController = navController,
        onReturnButtonClicked = vm::gotoMainScreen,
    )
}

//region Internal implementation
@Composable
internal fun EshareConnectionStoppedScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onReturnButtonClicked: OnNavigationCallback? = null,
) {
    // Retrieve margin values from resources
    val headerTopMargin = dimensionResource(R.dimen.bt_disabled_header_top_margin)
    val bodyTopMargin = dimensionResource(R.dimen.bt_disabled_body_top_margin)

    //TODO: Make this into a reusable component
    BaseScreen(
        modifier = modifier,
        showBackButton = false,
        showSettingsButton = false,
        onBackButtonInvoked = { },
        onSettingsButtonInvoked = { },
        bottomButton = { },
    ) {
        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (icon, header, subheader, button) = createRefs()

            // Set up the big Bluetooth icon
            BigIcon(painter = painterResource(R.drawable.link_off),
                contentDescription = "Disconnected chain link",
                modifier = modifier.constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            // Set up the header text
            Header1Text(
                text = stringResource(R.string.eshare_stopped_header),
                modifier = modifier.constrainAs(header) {
                    top.linkTo(icon.bottom, margin = headerTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            )

            // Set up the body text
            Subheader(
                text = stringResource(R.string.eshare_stopped_subheader),
                modifier = modifier
                    .padding(
                        dimensionResource(R.dimen.bt_disabled_horizontal_padding),
                        dimensionResource(R.dimen.zero)
                    )
                    .constrainAs(subheader) {
                        top.linkTo(header.bottom, margin = bodyTopMargin)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                textAlign = TextAlign.Center
            )

            TextRectangularButton(
                onClick = { onReturnButtonClicked?.invoke(navController) },
                modifier = modifier.constrainAs(button) {
                    top.linkTo(subheader.bottom, margin = 50.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                },
                text = stringResource(R.string.eshare_stopped_button)
            )
        }
    }
}

@Preview
@Composable
internal fun ConnectionStoppedPreview() {
    EshareConnectionStoppedScreen()
}
//endregion
