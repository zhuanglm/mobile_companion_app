package com.esightcorp.mobile.app.ui.components.buttons.bottomButtons

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.esightcorp.mobile.app.ui.R
import com.esightcorp.mobile.app.ui.navigation.OnActionCallback

//TODO: Icon rendering is all black for some reason, probably inherited properties
@Composable
fun SetupHotspotButton(
    modifier: Modifier = Modifier,
    onClick: OnActionCallback,
) = SupportButtonTemplate(
    modifier = modifier,
    onClick = onClick,
    text = stringResource(R.string.kEshareTroubleshootingUnableToConnectFooterButtonText),
    painter = painterResource(R.drawable.round_question_mark_24)
)
