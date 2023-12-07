package com.esightcorp.mobile.app.companion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.companion.navigation.toplevel.TopLevelNavigation
import com.esightcorp.mobile.app.ui.components.theme.Mobile_companion_appTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DelegatorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DelegatorActivity", "onCreate: ")
        setContent {
            CompanionApp()
        }
    }

}

@Composable
private fun CompanionApp() {
    Mobile_companion_appTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                TopLevelNavigation()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    CompanionApp()
}
