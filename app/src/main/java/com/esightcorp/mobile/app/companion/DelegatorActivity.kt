package com.esightcorp.mobile.app.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.esightcorp.mobile.app.companion.navigation.GoNavigation
import com.esightcorp.mobile.app.companion.ui.theme.Mobile_companion_appTheme

class ComposeActivity : ComponentActivity() {
    private val modules = arrayListOf("go", "nextgen")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoCompanionApp(modules = modules)
        }
    }
}

@Composable
fun GoCompanionApp(modules: ArrayList<String>){
    Mobile_companion_appTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                 GoNavigation()
            }
//            generateModuleButtons(modules)
        }
    }
}


@Composable
fun moduleButton(module: String){
    Button(onClick = {  }) {
        Text(text = module, color = Color.White)
    }
}

@Composable
fun generateModuleButtons(modules: ArrayList<String>){
    Column (modifier = Modifier
        .fillMaxSize(),
    verticalArrangement = Arrangement.Center) {
        for (module in modules){
            moduleButton(module)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Mobile_companion_appTheme {
        generateModuleButtons(arrayListOf<String>("go", "nextgen"))
    }
}