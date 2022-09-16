package com.esightcorp.mobile.app.companion.navigation

sealed class SupportedProducts(val route:String) {
    object SelectionScreen: SupportedProducts("selectionScreen")
    object GoProduct: SupportedProducts("go")
    object E4Product: SupportedProducts("e4")
    object NextGenProduct: SupportedProducts("nextgen")
}