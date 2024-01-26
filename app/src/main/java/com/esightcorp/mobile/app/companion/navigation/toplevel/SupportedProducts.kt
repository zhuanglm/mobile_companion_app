/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.companion.navigation.toplevel

sealed class SupportedProducts(val route: String) {
    object SelectionScreen : SupportedProducts("selectionScreen")

    object GoProduct :
        SupportedProducts(com.esightcorp.mobile.app.ui.navigation.GoProduct.IncomingRoute.path)

    object E4Product : SupportedProducts("e4")
    object NextGenProduct : SupportedProducts("nextgen")
}