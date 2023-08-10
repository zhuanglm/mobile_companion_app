package com.esightcorp.mobile.app.networking


// The fully composed listener
interface WifiModelListener : NetworkListener, SystemStatusListener, ErrorListener,
    SpecialCaseListener
