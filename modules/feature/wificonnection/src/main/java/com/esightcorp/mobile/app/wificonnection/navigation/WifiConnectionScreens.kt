/*
 * LICENSE
 * Copyright (C) 2009-2024 by eSight by Gentex Corporation. All Rights Reserved.
 * The software and information contained herein are proprietary to, and
 * comprise valuable trade secrets of, eSight by Gentex Corporation, which intends to
 * preserve as trade secrets such software and information.
 */

package com.esightcorp.mobile.app.wificonnection

@Deprecated("TODO: Migrate to WifiNavigation")
sealed class WifiConnectionScreens(val route: String) {

    object ConnectingRoute : WifiConnectionScreens("connecting")

    object WifiQRCodeRoute : WifiConnectionScreens("wifi_qrcode")
    object AdvancedNetworkSettingsRoute : WifiConnectionScreens("advanced_network_settings")
    object SelectNetworkSecurityRoute : WifiConnectionScreens("select_network_security")
}
