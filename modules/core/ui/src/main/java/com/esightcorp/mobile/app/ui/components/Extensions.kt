package com.esightcorp.mobile.app.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Redirect user to the web browser at the `url` page
 *
 * @param url The target page
 */
fun Context.openExternalUrl(url: String) = try {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }

    startActivity(intent)

} catch (err: Throwable) {
    err.printStackTrace()
}