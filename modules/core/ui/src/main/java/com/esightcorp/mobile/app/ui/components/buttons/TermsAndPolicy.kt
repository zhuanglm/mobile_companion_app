package com.esightcorp.mobile.app.ui.components.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.esightcorp.mobile.app.ui.components.text.ClickableBodyText

@Composable
fun TermsAndPolicy(
    onTermsInvoked: (Int) -> Unit,
    onPrivacyPolicyInvoked: (Int) -> Unit,
    modifier: Modifier,
    textColor: Color
){
    Column(modifier = modifier.fillMaxWidth()) {
        ClickableBodyText(
            text = "By continuing, you agree to our Terms.",
            modifier = modifier.fillMaxWidth(),
            onClick = onTermsInvoked,
            color = textColor
        )
        ClickableBodyText(
            text = "See how we use your data in our Privacy Policy",
            modifier = modifier.fillMaxWidth(),
            onClick = onPrivacyPolicyInvoked,
            color = textColor
        )
    }

}