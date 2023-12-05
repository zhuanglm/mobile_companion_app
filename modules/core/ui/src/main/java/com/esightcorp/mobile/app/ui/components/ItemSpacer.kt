package com.esightcorp.mobile.app.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.esightcorp.mobile.app.ui.R

@Composable
fun ItemSpacer(space: Dp = dimensionResource(R.dimen.screen_item_spacer)) =
    Spacer(modifier = Modifier.height(space))

