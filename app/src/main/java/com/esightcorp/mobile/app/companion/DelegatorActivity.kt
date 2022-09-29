package com.esightcorp.mobile.app.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.esightcorp.mobile.app.companion.navigation.toplevel.TopLevelNavigation
import com.esightcorp.mobile.app.companion.ui.theme.Mobile_companion_appTheme

class DelegatorActivity : ComponentActivity() {
    private val modules = arrayListOf("go", "nextgen")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompanionApp(modules = modules)
        }
    }
}

@Composable
fun CompanionApp(modules: ArrayList<String>){
    Mobile_companion_appTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                 TopLevelNavigation()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CompanionApp(arrayListOf("one", "two"))
}