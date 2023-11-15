package com.esightcorp.mobile.app.ui

import androidx.compose.foundation.border
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Utility to help debugging a view's border
 */
fun Modifier.debugBorder(color: Color = Color.Red) = this.border(width = 1.dp, color = color)
