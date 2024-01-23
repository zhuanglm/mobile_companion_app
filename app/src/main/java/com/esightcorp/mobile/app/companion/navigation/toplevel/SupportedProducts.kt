package com.esightcorp.mobile.app.companion.navigation.toplevel

sealed class SupportedProducts(val route: String) {
    object SelectionScreen : SupportedProducts("selectionScreen")

    object GoProduct :
        SupportedProducts(com.esightcorp.mobile.app.ui.navigation.GoProduct.IncomingRoute.path)

    object E4Product : SupportedProducts("e4")
    object NextGenProduct : SupportedProducts("nextgen")
}