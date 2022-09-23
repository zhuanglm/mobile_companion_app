package com.esightcorp.mobile.app.companion

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    //empty application file, so that Hilt will work
}