package com.esightcorp.mobile.app.ui.components.error_screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.components.text.Header1Text
import com.esightcorp.mobile.app.ui.components.text.Subheader
import com.esightcorp.mobile.app.ui.components.containers.BaseScreen
import com.esightcorp.mobile.app.ui.components.icons.BigIcon


//Draft state, throwing an error based on infinite height when trying to preview

@Composable
fun BigIconBase(
    modifier: Modifier = Modifier,
    onBackButtonClicked: () -> Unit = {Unit},
    onSettingsButtonClicked: () -> Unit = {Unit},
    showBackButton: Boolean = false,
    showSettingsButton: Boolean = false,
    bottomButton: @Composable () -> Unit = {Unit},
    content: @Composable () -> Unit
) {

    val headerTopMargin = dimensionResource(id = R.dimen.bt_disabled_header_top_margin)
    val bodyTopMargin = dimensionResource(id = R.dimen.bt_disabled_body_top_margin)


    BaseScreen(modifier = modifier,
        showBackButton = false,
        showSettingsButton = false,
        onBackButtonInvoked = onBackButtonClicked,
        onSettingsButtonInvoked = { /*Unused*/ },
        bottomButton = {Unit}) {

        ConstraintLayout(modifier = modifier.fillMaxSize()) {
            val (icon, header, subheader, everything) = createRefs()
            // Set up the big Bluetooth icon
            BigIcon(
                painter = painterResource(id = R.drawable.baseline_bluetooth_24),
                contentDescription = stringResource(R.string.content_desc_bt_icon),
                modifier = modifier.constrainAs(icon) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                })

            // Set up the header text
            Header1Text(
                text = stringResource(id = R.string.kBTErrorBluetoothOffTitle),
                modifier = modifier.constrainAs(header) {
                    top.linkTo(icon.bottom, margin = headerTopMargin)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            // Set up the body text
            Subheader(
                text = stringResource(id = R.string.kBTErrorBluetoothOffDescription),
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

            Box(modifier = modifier
                .constrainAs(everything){
                    top.linkTo(subheader.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .padding(25.dp, 0.dp, 25.dp, 0.dp).fillMaxSize()
            ) {
                LazyColumn(modifier = modifier) {
                    item {
                        content()
                    }
                }
            }
        }
    }
}
