package com.esightcorp.mobile.app.wificonnection

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.BigIcon
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Header2Text
import com.esightcorp.mobile.app.wificonnection.viewmodels.WifiOffViewModel

@Composable
fun WifiOffRoute(
    navController: NavController,
    vm: WifiOffViewModel = hiltViewModel()
){
    val TAG = "WifiOffRoute"
    WifiOffScreen(
        navController = navController,
        vm = vm,
        onBackPressed = { navController.popBackStack() },
    )

}

@Composable
internal fun WifiOffScreen(
    navController: NavController,
    vm: WifiOffViewModel,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = { navController.popBackStack() }
) {
    val TAG = "WifiOffScreen"
    val headerTopMargin = dimensionResource(id = R.dimen.bt_disabled_header_top_margin)
    val bodyTopMargin = dimensionResource(id = R.dimen.bt_disabled_body_top_margin)

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.surface) {
        ConstraintLayout {
            val (topBar, bigIcon, headerText, header2Text) = createRefs()
            ESightTopAppBar(
                showBackButton = true,
                showSettingsButton = false,
                onBackButtonInvoked = onBackPressed ,
                onSettingsButtonInvoked = {/*Unused*/ },
                modifier = modifier.constrainAs(topBar) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            BigIcon(
                painter = painterResource(id = R.drawable.round_wifi_24),
                contentDescription = stringResource(R.string.content_desc_wifi_icon),
                modifier = modifier.constrainAs(bigIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })

            Header1Text(
                text = stringResource(id = R.string.wifi_disabled_header),
                modifier = modifier.constrainAs(headerText) {
                    top.linkTo(bigIcon.bottom, margin = headerTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header2Text(
                text = stringResource(id = R.string.wifi_disabled_body),
                modifier = modifier
                    .padding(
                        dimensionResource(id = R.dimen.bt_disabled_horizontal_padding),
                        dimensionResource(
                            id = R.dimen.zero
                        )
                    )
                    .constrainAs(header2Text) {
                        top.linkTo(headerText.bottom, margin = bodyTopMargin)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                textAlign = TextAlign.Center

            )
        }
    }

}