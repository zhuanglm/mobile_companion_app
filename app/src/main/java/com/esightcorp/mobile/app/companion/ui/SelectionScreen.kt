package com.esightcorp.mobile.app.companion.navigation

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SelectionScreen(navController: NavController){
    Button(onClick = {navController.navigate("go")}) {
        Text(text = "Go")
    }

}