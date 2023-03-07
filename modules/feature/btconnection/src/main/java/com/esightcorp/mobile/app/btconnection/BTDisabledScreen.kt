package com.esightcorp.mobile.app.btconnection

import android.util.Log
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
import com.esightcorp.mobile.app.btconnection.viewmodels.BtDisabledViewModel
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.BigIcon
import com.esightcorp.mobile.app.ui.components.ESightTopAppBar
import com.esightcorp.mobile.app.ui.components.Header1Text
import com.esightcorp.mobile.app.ui.components.Header2Text

@Composable
fun BtDisabledScreen(
    navController: NavController,
    vm: BtDisabledViewModel = hiltViewModel()
) {
    val TAG = "BtDisabledScreen"
    Log.d(TAG, "BtDisabledScreen: ")
    BtDisabledScreen(onBackPressed = {}, modifier = Modifier)
    // TODO: Pick up as part of the ticket EG-1025 - Edge cases for bluetooth

}

@Composable
internal fun BtDisabledScreen(
    onBackPressed: () -> Unit,
    modifier: Modifier
) {
    /*
    Importing these are variables since the margin function does not accept @Composables
     */
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
                painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                contentDescription = stringResource(R.string.content_desc_bt_icon),
                modifier = modifier.constrainAs(bigIcon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })

            Header1Text(
                text = stringResource(id = R.string.bt_disabled_header),
                modifier = modifier.constrainAs(headerText) {
                    top.linkTo(bigIcon.bottom, margin = headerTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            Header2Text(
                text = stringResource(id = R.string.bt_disabled_body),
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